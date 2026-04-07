package com.acmecorp.events;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.acmecorp.events.Controllers.BookingController;
import com.acmecorp.events.Controllers.EventPerformanceController;
import com.acmecorp.events.Controllers.MenuController;
import com.acmecorp.events.Controllers.UserController;
import com.acmecorp.events.Models.Booking;
import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.Event;
import com.acmecorp.events.Models.EventType;
import com.acmecorp.events.Models.Performance;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Services.MockPaymentSystem;
import com.acmecorp.events.Services.MockVerificationSystem;
import com.acmecorp.events.Services.PaymentSystem;
import com.acmecorp.events.Views.View;

public class CancelPerformanceSystemTests {
    private static class FakeView implements View {
        private final Map<String, ArrayDeque<String>> inputs = new LinkedHashMap<>();
        private final List<String> succ = new ArrayList<>();
        private final List<String> errs = new ArrayList<>();

        void addInput(String key, String... values) {
            ArrayDeque<String> queue = this.inputs.computeIfAbsent(key,
                unused -> new ArrayDeque<>());
            for (String value : values) {
                queue.add(value);
            }
        }

        @Override
        public String getInput(String prompt) {
            for (Map.Entry<String, ArrayDeque<String>> entry : this.inputs.entrySet()) {
                if (prompt.contains(entry.getKey())) {
                    if (entry.getValue().isEmpty()) {
                        throw new AssertionError("No input left for prompt "
                            + prompt);
                    }
                    return entry.getValue().removeFirst();
                }
            }
            throw new AssertionError("Unexpected prompt " + prompt);
        }

        @Override
        public void displaySuccess(String msg) {
            this.succ.add(msg);
        }

        @Override
        public void displayError(String msg) {
            this.errs.add(msg);
        }

        @Override
        public void displayListofPerformances(
            Collection<String> list) {
        }

        @Override
        public void displaySpecificPerformance(String info) {
        }

        @Override
        public void displayBookingRecord(String rec) {
        }
    }

    private record Setup(FakeView view, UserController uc, BookingController bc,
        EventPerformanceController ec, MenuController mc, List<Event> events) {}

    private Setup make(PaymentSystem pay) {
        FakeView view = new FakeView();
        List<Event> events = new ArrayList<>();
        User user = null;
        UserController uc = new UserController(view, new MockVerificationSystem(),
            user);
        BookingController bc = new BookingController(view, user, events, pay);
        EventPerformanceController ec = new EventPerformanceController(view, user,
            events, pay);
        MenuController mc = new MenuController(uc, bc, ec, view, user);
        return new Setup(view, uc, bc, ec, mc, events);
    }

    private Performance addPerf(List<Event> events, EntertainmentProvider ep,
        long id, LocalDateTime start) {
        Event e = new Event(1L + id, "Fringe Event 2", EventType.MUSIC,
            true, new ArrayList<>(), ep);
        events.add(e);
        return e.createPerformance(e, id, start, start.plusHours(2),
            List.of("A", "B"), 25.0, 10, "123 Festival Street", 400,
            false, false);
    }

    private void setEp(EntertainmentProvider ep, Setup setup) {
        setup.uc().setCurrentUser(ep);
        setup.bc().setCurrentUser(ep);
        setup.ec().setCurrentUser(ep);
        setup.mc().setCurrentUser(ep);
    }

    @Test
    void cancelPerfSuccessTest() {
        MockPaymentSystem pay = new MockPaymentSystem(new ArrayList<>());
        Setup setup = make(pay);
        EntertainmentProvider ep = new EntertainmentProvider(
            "admin@edinburghtheatre.com", "editheatre", "edi12345",
            "Edinburgh Theatres and Performing Arts", "Liam Theatremaster",
            "Edinburgh Theatres");
        Performance p = addPerf(setup.events(), ep, 10L,
            LocalDateTime.now().plusDays(5));
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        Booking b = new Booking(1L, 2, 50.0, LocalDateTime.now(), s, p);
        p.addBooking(b);
        p.setNumTicketsSold(2);
        pay.processPayment(2, p.getEventTitle(), s.getEmail(), s.getPhoneNumber(),
            ep.getEmail(), 50.0);
        setEp(ep, setup);
        setup.view().addInput("Menu", "5", "0");
        setup.view().addInput("ID of the performance", "10");
        setup.view().addInput("message for affected students",
            "Cancelled by organiser");

        setup.mc().mainMenu();

        assertTrue(setup.view().succ.contains(
            "Cancellation Successful!"),
            "Cancellation success message was not shown.");
        assertEquals(Performance.PerformanceStatus.CANCELLED, p.getStatus(),
            "Performance was not cancelled.");
        assertEquals(Booking.BookingStatus.CANCELLED_BY_PROVIDER, b.getStatus(),
            "Booking was not cancelled by provider.");
    }

    @Test
    void cancelPerfWrongProvThenSuccessTest() {
        MockPaymentSystem pay = new MockPaymentSystem(new ArrayList<>());
        Setup setup = make(pay);
        EntertainmentProvider owner = new EntertainmentProvider(
            "admin@edinburghtheatre.com", "editheatre", "edi12345",
            "Edinburgh Theatres and Performing Arts", "Liam Theatremaster",
            "Edinburgh Theatres");
        EntertainmentProvider other = new EntertainmentProvider(
            "sales@comart.org.uk", "comart57", "sp24680a",
            "Official Community Art Centre", "Polly K", "Community Art Centre");
        addPerf(setup.events(), owner, 10L, LocalDateTime.now().plusDays(5));
        addPerf(setup.events(), other, 11L, LocalDateTime.now().plusDays(5));
        setEp(owner, setup);
        setup.view().addInput("Menu", "5", "0");
        setup.view().addInput("ID of the performance", "11", "10");
        setup.view().addInput("message for affected students",
            "Cancelled by organiser");

        setup.mc().mainMenu();

        assertEquals(1, Collections.frequency(setup.view().errs,
            "Performance was not created by the current entertainment provider."),
            "Wrong provider error was not shown.");
        assertTrue(setup.view().succ.contains(
            "Cancellation Successful!"),
            "Cancellation did not succeed after retry.");
    }

    @Test
    void cancelPerfAlreadyCancelledThenSuccessTest() {
        MockPaymentSystem pay = new MockPaymentSystem(new ArrayList<>());
        Setup setup = make(pay);
        EntertainmentProvider ep = new EntertainmentProvider(
            "admin@edinburghtheatre.com", "editheatre", "edi12345",
            "Edinburgh Theatres and Performing Arts", "Liam Theatremaster",
            "Edinburgh Theatres");
        Performance cancelled = addPerf(setup.events(), ep, 10L,
            LocalDateTime.now().plusDays(5));
        cancelled.cancel();
        addPerf(setup.events(), ep, 11L, LocalDateTime.now().plusDays(6));
        setEp(ep, setup);
        setup.view().addInput("ID of the performance", "10", "11");
        setup.view().addInput("message for affected students",
            "Cancelled by organiser");

        setup.ec().cancelPerformance();

        assertEquals(1, Collections.frequency(setup.view().errs,
            "Performance is already cancelled"),
            "Already cancelled performance was not rejected.");
        assertTrue(setup.view().succ.contains(
            "Cancellation Successful!"),
            "Cancellation did not succeed after retry.");
    }

    @Test
    void cancelPerfRefundFailStateUnchangedTest() {
        PaymentSystem pay = new PaymentSystem() {
            @Override
            public boolean processPayment(int numTix, String title,
                String stuEmail, String stuPhone, String epEmail,
                double amt) {
                return true;
            }

            @Override
            public boolean processRefund(int numTix, String title,
                String stuEmail, String stuPhone, String epEmail,
                double amt, String msg) {
                return numTix == 1;
            }
        };
        Setup setup = make(pay);
        EntertainmentProvider ep = new EntertainmentProvider(
            "admin@edinburghtheatre.com", "editheatre", "edi12345",
            "Edinburgh Theatres and Performing Arts", "Liam Theatremaster",
            "Edinburgh Theatres");
        Performance p = addPerf(setup.events(), ep, 10L,
            LocalDateTime.now().plusDays(5));
        Student s1 = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        Student s2 = new Student("jane@hindeburgh.ac.uk", "abc123",
            "Jane Smith", "07401176330");
        Booking b1 = new Booking(1L, 1, 25.0, LocalDateTime.now(), s1, p);
        Booking b2 = new Booking(2L, 2, 50.0, LocalDateTime.now(), s2, p);
        p.addBooking(b1);
        p.addBooking(b2);
        setEp(ep, setup);
        setup.view().addInput("ID of the performance", "10");
        setup.view().addInput("message for affected students",
            "Cancelled by organiser");

        setup.ec().cancelPerformance();

        assertEquals(1, Collections.frequency(setup.view().errs,
            "There was an issue with a refund. The performance cannot be cancelled."),
            "Refund failure was not reported.");
        assertEquals(Performance.PerformanceStatus.ACTIVE, p.getStatus(),
            "Performance should have stayed active.");
        assertEquals(Booking.BookingStatus.ACTIVE, b1.getStatus(),
            "First booking should have stayed active.");
        assertEquals(Booking.BookingStatus.ACTIVE, b2.getStatus(),
            "Second booking should have stayed active.");
    }
}
