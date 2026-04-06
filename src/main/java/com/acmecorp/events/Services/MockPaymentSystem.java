package com.acmecorp.events.Services;

import com.acmecorp.events.Models.Booking;
import java.util.List;

public class MockPaymentSystem implements PaymentSystem {

    private final List<Booking> bookings;

    public MockPaymentSystem(List<Booking> bookings) {
        this.bookings = bookings;
    }

    private boolean verifyInput(int numTickets, double transactionAmount) {
        return numTickets >= 1 && transactionAmount > 0;
    }

    private boolean matchesKnownValidPayment(int numTickets, String eventTitle, String studentEmail,
                                             String studentPhone, String epEmail, double transactionAmount) {

        return
            (numTickets == 1
                && "Fringe Event 2".equals(eventTitle)
                && "john@hindeburgh.ac.uk".equals(studentEmail)
                && "07455753486".equals(studentPhone)
                && "admin@edinburghtheatre.com".equals(epEmail)
                && transactionAmount == 25.00)

            ||

            (numTickets == 2
                && "Grand Circus".equals(eventTitle)
                && "jane@hindeburgh.ac.uk".equals(studentEmail)
                && "07401176330".equals(studentPhone)
                && "circus@londoncircus.com".equals(epEmail)
                && transactionAmount == 150.00)

            ||

            (numTickets == 3
                && "Abstract Exhibition".equals(eventTitle)
                && "james@hindeburgh.ac.uk".equals(studentEmail)
                && "07403937357".equals(studentPhone)
                && "sales@comart.org.uk".equals(epEmail)
                && transactionAmount == 30.00);
    }

    @Override
    public Boolean processPayment(int numTickets, String eventTitle, String studentEmail,
                                  String studentPhone, String epEmail, double transactionAmount) {

        if (!verifyInput(numTickets, transactionAmount)) {
            return false;
        }

        bookings.add(new Booking(eventTitle, studentEmail));
        return true;
    }

    @Override
    public Boolean processRefund(int numTickets, String eventTitle, String studentEmail,
                                 String studentPhone, String epEmail, double transactionAmount,
                                 String organiserMsg) {

        if (!verifyInput(numTickets, transactionAmount)) {
            return false;
        }

        if (!matchesKnownValidPayment(
                numTickets, eventTitle, studentEmail, studentPhone, epEmail, transactionAmount)) {
            return false;
        }

        Booking booking = new Booking(eventTitle, studentEmail);

        if (!bookings.contains(booking)) {
            return false;
        }

        bookings.remove(booking);
        return true;
    }
}