package com.acmecorp.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.FieldSource;

import com.acmecorp.events.Services.MockPaymentSystem;

public class TestsMockPaymentSystem {

    static List<Arguments> VALID_PAYMENTS = Arrays.asList(
        arguments(1, "Fringe Event 2", "john@hindeburgh.ac.uk",
            "07455753486", "admin@edinburghtheatre.com", 25.00),
        arguments(2, "Grand Circus", "jane@hindeburgh.ac.uk",
            "07401176330", "circus@londoncircus.com", 150.00),
        arguments(3, "Abstract Exhibition", "james@hindeburgh.ac.uk",
            "07403937357", "sales@comart.org.uk", 30.00)
    );

    static List<Arguments> NUMERICAL_INVALID_PAYMENTS = Arrays.asList(
        arguments(0, "Fringe Event 2", "john@hindeburgh.ac.uk",
            "07455753486", "admin@edinburghtheatre.com", 25.00),
        arguments(-23, "Grand Circus", "jane@hindeburgh.ac.uk",
            "07401176330", "circus@londoncircus.com", -1.35),
        arguments(3, "Abstract Exhibition", "james@hindeburgh.ac.uk",
            "07403937357", "sales@comart.org.uk", 0)
    );

    static List<Arguments> DETAILS_INVALID_PAYMENTS = Arrays.asList(
        arguments(2, "Fringe Event 2", "john@hindeburgh.ac.uk", //wrong ticket count
            "07455753486", "admin@edinburghtheatre.com", 25.00),
        arguments(2, "Grande Epee", "jane@hindeburgh.ac.uk", //wrong event name
            "07401176330", "circus@londoncircus.com", 150.00),
        arguments(3, "Abstract Exhibition", "jeeves@hindeburgh.ac.uk", //wrong email
            "07403937357", "sales@comart.org.uk", 30.00),
        arguments(1, "Fringe Event 2", "john@hindeburgh.ac.uk", //wrong phone number
            "03431234132", "admin@edinburghtheatre.com", 25.00), 
        arguments(2, "Grand Circus", "jane@hindeburgh.ac.uk", //wrong EP email
            "07401176330", "circus@grandcircus.com", 150.00),
        arguments(3, "Abstract Exhibition", "james@hindeburgh.ac.uk", //wrong payment amount
            "07403937357", "sales@comart.org.uk", 13.37)
    );

    MockPaymentSystem prePopulateMockPaymentSystemWithValid(){
        ArrayList<MockPaymentSystem.Booking> initialBookings = new ArrayList<>();
        for (Arguments a : VALID_PAYMENTS){
            Object[] arguments = a.get();
            initialBookings.add(new MockPaymentSystem.Booking((int)arguments[0], (String)arguments[1], (String)arguments[2],
                (String)arguments[3], (String)arguments[4], (double)arguments[5]
            ));
        }
        return new MockPaymentSystem(initialBookings);
    }

    /**Check that valid payments are handled by processPayment properly*/
    @ParameterizedTest
    @FieldSource("VALID_PAYMENTS")
    void validPaymentProcessPaymentTest(int numTickets, String eventTitle, String studentEmail, 
        String studentPhone, String epEmail, double transactionAmount){
            MockPaymentSystem mockPaymentSystem = new MockPaymentSystem(new ArrayList<>());
            assertTrue(mockPaymentSystem.processPayment(numTickets, eventTitle, studentEmail, 
                studentPhone, epEmail, transactionAmount));
    }

    /** Check that numerically invalid payments are rejected by processPayment properly, 
     * e.g. ticket price or number of too small*/
    @ParameterizedTest
    @FieldSource("NUMERICAL_INVALID_PAYMENTS")
    void numericalInvalidPaymentProcessPaymentTest(int numTickets, String eventTitle, String studentEmail, 
        String studentPhone, String epEmail, double transactionAmount){
            MockPaymentSystem mockPaymentSystem = new MockPaymentSystem(new ArrayList<>());
            assertFalse(mockPaymentSystem.processPayment(numTickets, eventTitle, studentEmail, 
                studentPhone, epEmail, transactionAmount));
    }

    /**Check that already added payments can be refunded by processRefund*/
    @ParameterizedTest
    @FieldSource("VALID_PAYMENTS")
    void refundPaymentTest(int numTickets, String eventTitle, String studentEmail, 
        String studentPhone, String epEmail, double transactionAmount){
            MockPaymentSystem mockPaymentSystem = prePopulateMockPaymentSystemWithValid();
            assertTrue(mockPaymentSystem.processRefund(numTickets, eventTitle, studentEmail, 
                studentPhone, epEmail, transactionAmount, ""));
    }

    /**Check that payments with invalid details are rejected by processRefund properly*/
    @ParameterizedTest
    @FieldSource("DETAILS_INVALID_PAYMENTS")
    void detailsInvalidRefundPaymentTest(int numTickets, String eventTitle, String studentEmail, 
        String studentPhone, String epEmail, double transactionAmount){
            MockPaymentSystem mockPaymentSystem = prePopulateMockPaymentSystemWithValid();
            assertFalse(mockPaymentSystem.processRefund(numTickets, eventTitle, studentEmail, 
                studentPhone, epEmail, transactionAmount, ""));
    }

    /**Check that payments with invalid numerical details are rejected by processRefund properly*/
    @ParameterizedTest
    @FieldSource("NUMERICAL_INVALID_PAYMENTS")
    void numericalInvalidRefundPaymentTest(int numTickets, String eventTitle, String studentEmail, 
        String studentPhone, String epEmail, double transactionAmount){
            MockPaymentSystem mockPaymentSystem = prePopulateMockPaymentSystemWithValid();
            assertFalse(mockPaymentSystem.processRefund(numTickets, eventTitle, studentEmail, 
                studentPhone, epEmail, transactionAmount, ""));
    }
}
