package com.acmecorp.events;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.FieldSource;

import com.acmecorp.events.Models.Booking;
import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.Event;
import com.acmecorp.events.Models.Performance;
import com.acmecorp.events.Models.Student;


public class TestsPerformance {
    Performance performance;

    @BeforeEach 
    void initNewPerformance(){
        EntertainmentProvider ep = new EntertainmentProvider("test@test.com", "test", "1", 
            "Test", "Test Test", "Test Inc.");
        Event event = new Event(1l, "Test Event", Event.EventType.DANCE, true, new ArrayList<>(), ep);
        this.performance = new Performance(
            event, 1, LocalDateTime.of(2026, 01, 01, 01, 01), LocalDateTime.of(2026, 01, 02, 01, 01),
            Arrays.asList(new String[]{"Test A", "Test B"}), 20.00, 400, "Test Address",
            1000, false, false
        );
    }

    /**
     * Check test behaves properly when cancelled
     */
    @Test
    void cancelTest(){
        this.performance.cancel();
        assertEquals(this.performance.getStatus(), Performance.PerformanceStatus.CANCELLED,
        "Performance was not marked as cancelled when cancelled.");
    }

    /**
     * Check if ticketed event is ticketed
     */
    @Test
    void checkIfEventIsTicktedTrueTest(){
        assertTrue((this.performance.getEvent().getTicketed()),
        "Ticketed event was erroneously marked as unticketed.");
    }

    /**
     * Check if unticked event is unticketed
     */
    @Test
    void checkIfEventIsTicktedFalseTest(){
        this.performance.getEvent().setTicketed(false);
        assertFalse((this.performance.getEvent().getTicketed()),
        "Unticketed event was erroneously marked as ticketed");
    }

    /**
     * Check if the ticket availiability checker returns true if given valid numbers
     */

    static List<Arguments> TICKETS_LEFT_TRUE = Arrays.asList(
        arguments(4, 7),
        arguments(1, 123),
        arguments(56, 57)
    );

    @ParameterizedTest
    @FieldSource("TICKETS_LEFT_TRUE")
    void checkIfTicketsLeftTrueTest(int wantedTickets, int leftTickets){
        this.performance.setNumTicketsTotal(leftTickets);
        assertTrue((this.performance.checkIfTicketsLeft(wantedTickets)),
        String.format("Purchaser could not buy %d tickets even though %d were left", wantedTickets, leftTickets));
    }

    /**
     * Check if the ticket availiability checker returns False if given numbers
     */

    static List<Arguments> TICKETS_LEFT_FALSE = Arrays.asList(
        arguments(57, 56),
        arguments(7, 5),
        arguments(546, 2)
    );

    @ParameterizedTest
    @FieldSource("TICKETS_LEFT_FALSE")
    void checkIfTicketsLeftFalseTest(int wantedTickets, int leftTickets){
        this.performance.setNumTicketsTotal(leftTickets);
        assertFalse((this.performance.checkIfTicketsLeft(wantedTickets)),
    String.format("Purchaser could buy %d tickets even though only %d were left", leftTickets, wantedTickets));
    }

    /**
     * Check if the total price is correctly calculated with sponsor discount by getFinalPrice
     */

    static List<Arguments> SPONSOR_AMOUNTS = Arrays.asList( //for 20.00 ticket
        arguments(0.00), arguments(0.01), arguments(5.00), arguments(15.00), arguments(19.99)
    );

    @ParameterizedTest
    @FieldSource("SPONSOR_AMOUNTS")
    void getFinalTicketPriceTest(double sponsorAmount){
        this.performance.sponsor(sponsorAmount);
        this.performance.setTicketPrice(20.00);
        double grandTotal = 20.00 - sponsorAmount;
        assertEquals(this.performance.getFinalTicketPrice(), grandTotal,
        String.format(
            "Grand total was incorrectly calculated as %.2f, in reality at a 20.00 price, %.2f sponsor amount, it should have been %.2f."
            , this.performance.getFinalTicketPrice(), sponsorAmount, grandTotal));
    }

    /**
     * Test if the function to retrieve the organiser's email is working.
     */
    @Test
    void getOrganiserEmailTest(){
        assertEquals(this.performance.getOrganiserEmail(), "test@test.com",
            "Wrong email or string was retrieved instead of organiser's email.");
    }

    /**
     * Test to see if the function to get the event title retrieves it properly
     */
    @Test
    void getEventTitelTest(){
        assertEquals(this.performance.getEventTitle(), "Test Event",
            "Wrong string was retrieved instead of organiser's title.");
    }

    /**
     * Test to see if the function can indentify and reject times in the past/already happened
     */

    static List<Arguments> PAST_TIMES = Arrays.asList( 
        arguments(LocalDateTime.of(2025, 6, 18, 7, 30)),
        arguments(LocalDateTime.of(2025, 7, 30, 15, 33)),
        arguments(LocalDateTime.of(2026, 1, 31, 23, 59))
        );

    @ParameterizedTest
    @FieldSource("PAST_TIMES")
    void checkHasNotHappenedYetFalse(LocalDateTime date){
        this.performance.setStartDateTime(date);
        assertFalse(this.performance.checkHasNotHappenedYet(),
        "Performance was marked as still to start despite already having finished.");
    }

    /**
     * Test to see if the function can identify and reject times in the past/already happened
     */

    static List<Arguments> FUTURE_TIMES = Arrays.asList( 
        arguments(LocalDateTime.of(2028, 11, 18, 16, 21)),
        arguments(LocalDateTime.of(2028, 4, 30, 21, 43)),
        arguments(LocalDateTime.of(2027, 5, 31, 10, 28))
        );

    @ParameterizedTest
    @FieldSource("FUTURE_TIMES")
    void checkHasNotHappenedYetTrue(LocalDateTime date){
        this.performance.setStartDateTime(date);
        assertTrue(this.performance.checkHasNotHappenedYet(),
        "Performance marked as already finished despite being yet to start");
    }
    
    /**
     * Test to see if the createdBYEp function works for a found EP, where the performance is done by the ep
     */

    static List<Arguments> EP_EMAILS = Arrays.asList( 
        arguments("e@e.mag"),
        arguments("varsity@variety.org"),
        arguments("admin@ep.com")
        );

    @ParameterizedTest
    @FieldSource("EP_EMAILS")
    void checkCreatedByEPTrueTest(String email){
        this.performance.getEvent().getOrganiser().setEmail(email);
        assertTrue(this.performance.checkCreatedByEP(email),
        "checkCreatedByEP returns false even though EP owns event.");
    }

    /**
     * Test to see if the createdBYEp function works for a not found EP, it should return false
     */

    @ParameterizedTest
    @FieldSource("EP_EMAILS")
    void checkCreatedByEPFalseTest(String email){
        assertFalse(this.performance.checkCreatedByEP(email),
        "checkCreatedByEP returns true even though EP does not own event.");
    }

    /**
     * Check if hasActiveBookings returns true if there are bookings
     */
    static List<Arguments> EXAMPLE_BOOKINGS = Arrays.asList(
        arguments(new Booking(1l, 5, 120.00, LocalDateTime.of(2025, 8, 23, 12, 31, 0), 
        new Student("john@hindeburgh.ac.uk","hunter1","John Smith","07455753486")), Booking.BookingStatus.CANCELLED_BY_PROVIDER),
        arguments(new Booking(2403l, 1, 2.75, LocalDateTime.of(2025, 10, 18, 20, 33, 0),
        new Student("jane@hindeburgh.ac.uk","abc123","Jane Smith","07401176330")), Booking.BookingStatus.CANCELLED_BY_USER),
        arguments(new Booking(7l, 20, 104.45, LocalDateTime.of(2026, 3, 4, 7, 59, 0),
        new Student("james@hindeburgh.ac.uk","mypassword","James Stuart","07403937357")), Booking.BookingStatus.PAYMENT_FAILED)
    );
    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void hasActiveBookingsTrueTest(Booking b){
        b.setStatus(Booking.BookingStatus.ACTIVE);
        this.performance.addBooking(b);
        assertTrue(this.performance.hasActiveBookings(),
        "Performance marked inactive while it has active bookings.");
    }

    /**
     * Check if hasActiveBookings returns false if there are only inactive bookings
     */
    @ParameterizedTest
    @FieldSource("EXAMPLE_BOOKINGS")
    void hasActiveBookingsFalseTest(Booking b, Booking.BookingStatus bs){
        b.setStatus(bs);
        this.performance.addBooking(b);
        assertFalse(this.performance.hasActiveBookings(),
        "Performance marked as active while it only has inactive bookings.");
    }

    /**
     * Check for the empty bookings case
     */

    @Test
    void hasActiveBookingsEmptyTest(){
        assertFalse(this.performance.hasActiveBookings(),
        "Performance marked as active even though it has empty bookings.");
    }

    /**
     * Check if sponsors are applied and are amounts are correct
     */

    @ParameterizedTest
    @FieldSource("SPONSOR_AMOUNTS")
    void checkCreatedByEPTrueTest(double sponsorAmount){
        this.performance.sponsor(sponsorAmount);
        assertEquals(this.performance.getSponsoredAmount(), sponsorAmount,
        "Sponsor amount does not match amount passed as parameter to function");
    }
    /**
     * Apart from above, getter and setter like methods are not unit tested, e.g. addBooking and toString (as a getter)
     */
}   