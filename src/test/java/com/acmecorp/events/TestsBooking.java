package com.acmecorp.events;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.FieldSource;

import com.acmecorp.events.Models.Booking;

public class TestsBooking {

    static List<Arguments> EXAMPLE_BOOKINGS = Arrays.asList(
        arguments(new Booking("lionking", "john@hindeburgh.ac.uk")),
        arguments(new Booking("hamilton", "jane@hindeburgh.ac.uk")),
        arguments(new Booking("wicked", "james@hindeburgh.ac.uk"))
    );

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void getPerformanceNameTest(Booking booking) {
        assertNotNull(booking.getPerformanceName());
    }

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void getStudentEmailTest(Booking booking) {
        assertNotNull(booking.getStudentEmail());
    }

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void checkPerformanceNameCorrectTest(Booking booking) {
        assertTrue(
            booking.getPerformanceName().equals("lionking") ||
            booking.getPerformanceName().equals("hamilton") ||
            booking.getPerformanceName().equals("wicked")
        );
    }

    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void checkStudentEmailContainsAtTest(Booking booking) {
        assertTrue(booking.getStudentEmail().contains("@"));
    }
}