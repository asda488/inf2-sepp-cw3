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

public class BookPerformanceSystemTests {
    private static class FakeView implements View {
        private final Map<String, ArrayDeque<String>> inputs = new LinkedHashMap<>();
        private final List<String> succ = new ArrayList<>();
        private final List<String> errs = new ArrayList<>();
        private final List<String> recs = new ArrayList<>();

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
                            + prompt + " errs " + this.errs
                            + " succ " + this.succ);
                    }
                    return entry.getValue().removeFirst();
                }
            }
            throw new AssertionError("Unexpected prompt " + prompt
                + " errs " + this.errs
                + " succ " + this.succ);
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
            this.recs.add(rec);
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

    private Performance addPerf(List<Event> events, boolean ticketed, long id,
        LocalDateTime start) {
        EntertainmentProvider ep = new EntertainmentProvider(
            "admin@edinburghtheatre.com", "editheatre", "edi12345",
            "Edinburgh Theatres and Performing Arts", "Liam Theatremaster",
            "Edinburgh Theatres");
        Event e = new Event(1L + id, "Fringe Event 2", EventType.MUSIC,
            ticketed, new ArrayList<>(), ep);
        events.add(e);
        return e.createPerformance(e, id, start, start.plusHours(2),
            List.of("A", "B"), 25.0, 10, "123 Festival Street", 400,
            false, false);
    }

    private void setStudent(Student s, Setup setup) {
        setup.uc().setCurrentUser(s);
        setup.bc().setCurrentUser(s);
        setup.ec().setCurrentUser(s);
        setup.mc().setCurrentUser(s);
    }

    @Test
    void bookPerfSuccessTest() {
        Setup setup = make(new MockPaymentSystem(new ArrayList<>()));
        addPerf(setup.events(), true, 10L, LocalDateTime.now().plusDays(2));
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        setStudent(s, setup);
        setup.view().addInput("Menu", "6", "0");
        setup.view().addInput("ID of the performance", "10");
        setup.view().addInput("number of tickets", "2");

        setup.mc().mainMenu();

        assertTrue(setup.view().succ.contains("Booking Successful!"),
            "Booking success message was not shown.");
        assertTrue(setup.view().recs.get(0)
            .contains("john@hindeburgh.ac.uk"),
            "Booking record did not include the student email.");
    }

    @Test
    void bookPerfInvalidThenSuccessTest() {
        Setup setup = make(new MockPaymentSystem(new ArrayList<>()));
        addPerf(setup.events(), true, 10L, LocalDateTime.now().plusDays(2));
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        setStudent(s, setup);
        setup.view().addInput("Menu", "6", "0");
        setup.view().addInput("ID of the performance", "999", "10");
        setup.view().addInput("number of tickets", "1");

        setup.mc().mainMenu();

        assertEquals(1, Collections.frequency(setup.view().errs,
            "Performance with given number does not exist."),
            "Missing invalid performance message.");
        assertTrue(setup.view().succ.contains("Booking Successful!"),
            "Booking did not succeed after retry.");
    }

    @Test
    void bookPerfUnticketedThenSuccessTest() {
        Setup setup = make(new MockPaymentSystem(new ArrayList<>()));
        addPerf(setup.events(), false, 10L, LocalDateTime.now().plusDays(2));
        addPerf(setup.events(), true, 11L, LocalDateTime.now().plusDays(2));
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        setStudent(s, setup);
        setup.view().addInput("Menu", "6", "0");
        setup.view().addInput("ID of the performance", "10", "11");
        setup.view().addInput("number of tickets", "2", "2");

        setup.mc().mainMenu();

        assertEquals(1, Collections.frequency(setup.view().errs,
            "The requested performance's event is not ticketed. There is no need to book it"),
            "Missing unticketed error.");
        assertTrue(setup.view().succ.contains("Booking Successful!"),
            "Booking did not succeed after unticketed retry.");
    }

    @Test
    void bookPerfCancelledThenSuccessTest() {
        Setup setup = make(new MockPaymentSystem(new ArrayList<>()));
        Performance cancelled = addPerf(setup.events(), true, 10L,
            LocalDateTime.now().plusDays(2));
        cancelled.cancel();
        addPerf(setup.events(), true, 11L, LocalDateTime.now().plusDays(3));
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        setStudent(s, setup);
        setup.view().addInput("ID of the performance", "10", "11");
        setup.view().addInput("number of tickets", "2");

        setup.bc().bookPerformance();

        assertEquals(1, Collections.frequency(setup.view().errs,
            "Performance is cancelled"),
            "Cancelled performance was not rejected.");
        assertTrue(setup.view().succ.contains("Booking Successful!"),
            "Booking did not succeed after cancelled retry.");
    }

    @Test
    void bookPerfPastThenSuccessTest() {
        Setup setup = make(new MockPaymentSystem(new ArrayList<>()));
        addPerf(setup.events(), true, 10L, LocalDateTime.now().minusDays(2));
        addPerf(setup.events(), true, 11L, LocalDateTime.now().plusDays(2));
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        setStudent(s, setup);
        setup.view().addInput("ID of the performance", "10", "11");
        setup.view().addInput("number of tickets", "2");

        setup.bc().bookPerformance();

        assertEquals(1, Collections.frequency(setup.view().errs,
            "Performance has already happened"),
            "Past performance was not rejected.");
        assertTrue(setup.view().succ.contains("Booking Successful!"),
            "Booking did not succeed after past retry.");
    }

    @Test
    void bookPerfPayFailTest() {
        PaymentSystem pay = new PaymentSystem() {
            @Override
            public boolean processPayment(int numTix, String title,
                String stuEmail, String stuPhone, String epEmail,
                double amt) {
                return false;
            }

            @Override
            public boolean processRefund(int numTix, String title,
                String stuEmail, String stuPhone, String epEmail,
                double amt, String msg) {
                return true;
            }
        };
        Setup setup = make(pay);
        addPerf(setup.events(), true, 10L, LocalDateTime.now().plusDays(2));
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        setStudent(s, setup);
        setup.view().addInput("Menu", "6", "0");
        setup.view().addInput("ID of the performance", "10");
        setup.view().addInput("number of tickets", "2");

        setup.mc().mainMenu();

        assertEquals(1, Collections.frequency(setup.view().errs,
            "There was an issue with payment."),
            "Payment failure was not reported.");
    }
}
