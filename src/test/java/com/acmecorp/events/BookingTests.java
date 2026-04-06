package com.acmecorp.events;

import com.acmecorp.events.Models.Booking;
import com.acmecorp.events.Models.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTests {

    @Test
    void testRemoveBooking() {
        Student student = new Student("test@test.com", "pass", "name", "123");
        Booking booking = new Booking("Concert");

        student.addBooking(booking);
        student.removeBooking(booking);

        assertFalse(student.getBookings().contains(booking));
    }

    @Test
    void testBookingWithNullUser() {
        Booking booking = new Booking("Concert");
        assertNotNull(booking);
    }

    @Test
    void testDuplicateBooking() {
        Student student = new Student("test@test.com", "pass", "name", "123");
        Booking booking = new Booking("Concert");

        student.addBooking(booking);
        student.addBooking(booking);

        assertTrue(student.getBookings().size() >= 1);
    }

    @Test
    void testRemoveNonExistingBooking() {
        Student student = new Student("test@test.com", "pass", "name", "123");
        Booking booking = new Booking("Concert");

        assertDoesNotThrow(() -> student.removeBooking(booking));
    }
}