package com.acmecorp.events;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.FieldSource;

import com.acmecorp.events.Models.Booking;
import com.acmecorp.events.Models.Student;

public class TestsBooking {
    static List<Arguments> EXAMPLE_BOOKINGS = Arrays.asList(
        arguments(new Booking(1l, 5, 120.00, LocalDateTime.of(2025, 8, 23, 12, 31, 0), 
        new Student("john@hindeburgh.ac.uk","hunter1","John Smith","07455753486")), 
        "john@hindeburgh.ac.uk", "07455753486"),
        arguments(new Booking(2403l, 1, 2.75, LocalDateTime.of(2025, 10, 18, 20, 33, 0),
        new Student("jane@hindeburgh.ac.uk","abc123","Jane Smith","07401176330")), 
        "jane@hindeburgh.ac.uk", "07401176330"),
        arguments(new Booking(7l, 20, 104.45, LocalDateTime.of(2026, 3, 4, 7, 59, 0),
        new Student("james@hindeburgh.ac.uk","mypassword","James Stuart","07403937357")), 
        "james@hindeburgh.ac.uk", "07403937357")
    );

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void cancelByStudentTest(Booking booking){
        booking.cancelByStudent();
        assertEquals(Booking.BookingStatus.CANCELLED_BY_USER, booking.getStatus(),
        "Booking could not be cancelled by student.");
    }

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void cancelPaymentFailedTest(Booking booking){
        booking.cancelPaymentFailed();
        assertEquals(Booking.BookingStatus.PAYMENT_FAILED, booking.getStatus(),
        "Booking could not be cancelled on payment failure.");
    }

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void cancelByProviderTest(Booking booking){
        booking.cancelByProvider();
        assertEquals(Booking.BookingStatus.CANCELLED_BY_PROVIDER, booking.getStatus(),
        "Booking could not be cancelled by provider/EP.");
    }

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void checkBookedByStudentTrueTest(Booking booking, String email){
        assertTrue(booking.checkBookedByStudent(email),
        String.format("Booking could not be attributed to %s even though it was booked with it.", email));
    }

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void checkBookedByStudentFalseTest(Booking booking){
        assertFalse(booking.checkBookedByStudent("false@gmail.com"),
        "Booking was attributed to different email even though it was not booked with it");
    }

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void getStudentDetailsCorrectDetailsTest(Booking booking, String email, String phoneNumber){
        String studentDetails = booking.getStudentDetails();
        assertAll(
            "Assertions for getStudentDetails:",
            () -> assertTrue(studentDetails.contains(email), 
                "Returned student details do not contain email."),
            () -> assertTrue(studentDetails.contains(phoneNumber), 
                "Returned student details do not contain phone number.")
        );
    }

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void generateBookingRecordTest(Booking booking, String email, String phoneNumber){
        String studentDetails = booking.generateBookingRecord();
        assertAll(
            "Assertions for getStudentDetails:",
            () -> assertTrue(studentDetails.contains(email), 
                "Returned student details do not contain email."),
            () -> assertTrue(studentDetails.contains(phoneNumber), 
                "Returned student details do not contain phone number.")
        );
    }

    //TODO: mockito test generateBookingRecord
}
