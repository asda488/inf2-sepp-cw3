package com.acmecorp.events;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.acmecorp.events.Models.Booking;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmecorp.events.Services.MockPaymentSystem;

public class TestMockPaymentSystem {

    static Stream<Arguments> validPayments() {
        return Stream.of(
            arguments(1, "Fringe Event 2", "john@hindeburgh.ac.uk",
                "07455753486", "admin@edinburghtheatre.com", 25.00),
            arguments(2, "Grand Circus", "jane@hindeburgh.ac.uk",
                "07401176330", "circus@londoncircus.com", 150.00),
            arguments(3, "Abstract Exhibition", "james@hindeburgh.ac.uk",
                "07403937357", "sales@comart.org.uk", 30.00)
        );
    }

    static Stream<Arguments> numericalInvalidPayments() {
        return Stream.of(
            arguments(0, "Fringe Event 2", "john@hindeburgh.ac.uk",
                "07455753486", "admin@edinburghtheatre.com", 25.00),
            arguments(-23, "Grand Circus", "jane@hindeburgh.ac.uk",
                "07401176330", "circus@londoncircus.com", -1.35),
            arguments(3, "Abstract Exhibition", "james@hindeburgh.ac.uk",
                "07403937357", "sales@comart.org.uk", 0.0)
        );
    }

    static Stream<Arguments> detailsInvalidPayments() {
        return Stream.of(
            arguments(2, "Fringe Event 2", "john@hindeburgh.ac.uk",
                "07455753486", "admin@edinburghtheatre.com", 25.00),
            arguments(2, "Grande Epee", "jane@hindeburgh.ac.uk",
                "07401176330", "circus@londoncircus.com", 150.00),
            arguments(3, "Abstract Exhibition", "jeeves@hindeburgh.ac.uk",
                "07403937357", "sales@comart.org.uk", 30.00),
            arguments(1, "Fringe Event 2", "john@hindeburgh.ac.uk",
                "03431234132", "admin@edinburghtheatre.com", 25.00),
            arguments(2, "Grand Circus", "jane@hindeburgh.ac.uk",
                "07401176330", "circus@grandcircus.com", 150.00),
            arguments(3, "Abstract Exhibition", "james@hindeburgh.ac.uk",
                "07403937357", "sales@comart.org.uk", 13.37)
        );
    }

    MockPaymentSystem prePopulateMockPaymentSystemWithValid() {
        ArrayList<Booking> initialBookings = new ArrayList<>();

        validPayments().forEach(a -> {
            Object[] args = a.get();
            initialBookings.add(new Booking(
                (String) args[1],
                (String) args[2]
            ));
        });

        return new MockPaymentSystem(initialBookings);
    }

    /** Check that valid payments are handled correctly */
    @ParameterizedTest
    @MethodSource("validPayments")
    void validPaymentProcessPaymentTest(int numTickets, String eventTitle, String studentEmail,
                                        String studentPhone, String epEmail, double transactionAmount) {

        MockPaymentSystem mockPaymentSystem = new MockPaymentSystem(new ArrayList<>());

        assertTrue(mockPaymentSystem.processPayment(
            numTickets, eventTitle, studentEmail,
            studentPhone, epEmail, transactionAmount
        ));
    }

    /** Check numerical invalid payments are rejected */
    @ParameterizedTest
    @MethodSource("numericalInvalidPayments")
    void numericalInvalidPaymentProcessPaymentTest(int numTickets, String eventTitle, String studentEmail,
                                                   String studentPhone, String epEmail, double transactionAmount) {

        MockPaymentSystem mockPaymentSystem = new MockPaymentSystem(new ArrayList<>());

        assertFalse(mockPaymentSystem.processPayment(
            numTickets, eventTitle, studentEmail,
            studentPhone, epEmail, transactionAmount
        ));
    }

    /** Check valid refunds succeed */
    @ParameterizedTest
    @MethodSource("validPayments")
    void refundPaymentTest(int numTickets, String eventTitle, String studentEmail,
                           String studentPhone, String epEmail, double transactionAmount) {

        MockPaymentSystem mockPaymentSystem = prePopulateMockPaymentSystemWithValid();

        assertTrue(mockPaymentSystem.processRefund(
            numTickets, eventTitle, studentEmail,
            studentPhone, epEmail, transactionAmount, ""
        ));
    }

    /** Check invalid detail refunds fail */
    @ParameterizedTest
    @MethodSource("detailsInvalidPayments")
    void detailsInvalidRefundPaymentTest(int numTickets, String eventTitle, String studentEmail,
                                         String studentPhone, String epEmail, double transactionAmount) {

        MockPaymentSystem mockPaymentSystem = prePopulateMockPaymentSystemWithValid();

        assertFalse(mockPaymentSystem.processRefund(
            numTickets, eventTitle, studentEmail,
            studentPhone, epEmail, transactionAmount, ""
        ));
    }

    /** Check invalid numerical refunds fail */
    @ParameterizedTest
    @MethodSource("numericalInvalidPayments")
    void numericalInvalidRefundPaymentTest(int numTickets, String eventTitle, String studentEmail,
                                           String studentPhone, String epEmail, double transactionAmount) {

        MockPaymentSystem mockPaymentSystem = prePopulateMockPaymentSystemWithValid();

        assertFalse(mockPaymentSystem.processRefund(
            numTickets, eventTitle, studentEmail,
            studentPhone, epEmail, transactionAmount, ""
        ));
    }
}