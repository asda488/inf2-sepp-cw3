package com.acmecorp.events.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.acmecorp.events.Models.Booking;
import com.acmecorp.events.Models.Event;
import com.acmecorp.events.Models.Performance;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Services.MockPaymentSystem;
import com.acmecorp.events.Services.PaymentSystem;
import com.acmecorp.events.Views.View;

public class BookingController extends Controller {
    private List<Event> evts;
    private List<Booking> books;
    private PaymentSystem paySys;
    private long nextBookNo;

    public BookingController(View view, User currentUser) {
        this(view, currentUser, new ArrayList<>(),
            new MockPaymentSystem(new ArrayList<>()));
    }

    public BookingController(View view, User currentUser, List<Event> evts,
        PaymentSystem paySys) {
        super(view, currentUser);
        this.evts = evts;
        this.paySys = paySys;
        this.books = new ArrayList<>();
        this.nextBookNo = 1;
    }

    public void bookPerformance() {
        assert this.checkCurrentUserIsStudent();
        Student s = (Student) this.currentUser;

        Performance p = null;
        int num = 0;
        while (p == null) {
            long id = readLong("Enter ID of the performance to book");
            p = getPerfByID(id);
            if (p == null) {
                this.view.displayError("Performance with given number does not exist.");
                continue;
            }
            if (p.getStatus() == Performance.PerformanceStatus.CANCELLED) {
                this.view.displayError("Performance is cancelled");
                p = null;
                continue;
            }
            if (!p.checkHasNotHappenedYet()) {
                this.view.displayError("Performance has already happened");
                p = null;
                continue;
            }
            if (!p.checkIfEventIsTicketed()) {
                this.view.displayError("The requested performance's event is not ticketed. There is no need to book it");
                p = null;
                continue;
            }
            num = readInt("Enter number of tickets");
            if (!p.checkIfTicketsLeft(num)) {
                this.view.displayError("Requested performance has no tickets left.");
                p = null;
            }
        }

        double paid = p.getFinalTicketPrice() * num;
        Booking b = new Booking(this.nextBookNo, num, paid,
            LocalDateTime.now(), s, p);
        this.nextBookNo++;
        this.books.add(b);
        s.addBooking(b);
        p.addBooking(b);

        boolean ok = this.paySys.processPayment(num, p.getEventTitle(),
            s.getEmail(), s.getPhoneNumber(), p.getOrganiserEmail(), paid);
        if (!ok) {
            b.cancelPaymentFailed();
            this.view.displayError("There was an issue with payment.");
            return;
        }

        p.setNumTicketsSold(p.getNumTicketsSold() + num);
        this.view.displaySuccess("Booking Successful!");
        this.view.displayBookingRecord(b.generateBookingRecord());
    }

    private Performance getPerfByID(long id) {
        for (Event e : evts) {
            for (Performance p : e.getPerformances()) {
                if (p.getPerformanceID() == id) {
                    return p;
                }
            }
        }
        return null;
    }

    private long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(this.view.getInput(prompt));
            } catch (NumberFormatException e) {
                this.view.displayError("Invalid number try again");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                int n = Integer.parseInt(this.view.getInput(prompt));
                if (n > 0) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // keep going
            }
            this.view.displayError("Invalid number try again");
        }
    }
}
