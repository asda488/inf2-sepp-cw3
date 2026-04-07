package com.acmecorp.events.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MockPaymentSystem implements PaymentSystem {

    public MockPaymentSystem(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public MockPaymentSystem() {
        this.bookings = new ArrayList<>();
    }

    /**
     * Service-internal Booking dataclass, not to be confused with the other Model class
     */
    public static class Booking {
        public final int numTickets;
        public final String eventTitle;
        public final String studentEmail;
        public final String studentPhone;
        public final String epEmail;
        public final double transactionAmount;

        public Booking(int numTickets, String eventTitle, String studentEmail, 
        String studentPhone, String epEmail, double transactionAmount) {
            this.epEmail = epEmail;
            this.eventTitle = eventTitle;
            this.numTickets = numTickets;
            this.studentEmail = studentEmail;
            this.studentPhone = studentPhone;
            this.transactionAmount = transactionAmount;
        }

        /** Overriding equals for easy comparison */
        @Override
        public boolean equals(Object o){
            if (o == this){
                return true;
            } else if(!(o instanceof Booking)){
                return false;
            }

            Booking b = (Booking)o;

            return b.epEmail.equals(this.epEmail)
                && b.eventTitle.equals(this.eventTitle)
                && b.numTickets == this.numTickets
                && b.studentEmail.equals(this.studentEmail)
                && b.studentPhone.equals(this.studentPhone)
                && b.transactionAmount == this.transactionAmount;
        }

        /** Override hashCode in line with equals */
        @Override
        public int hashCode(){
            return Objects.hash(this.epEmail, this.eventTitle, this.numTickets, 
                this.studentEmail, this.studentPhone, this.transactionAmount);
        }
    }

    private final List<Booking> bookings;

    /**
     * Perform sanity checks on input data to ensure it's well formed
     */
    private boolean verifyInput(int numTickets, double transactionAmount){
        return numTickets >= 1 && transactionAmount > 0;
    }

    /**
     * Given information for a single payment, check it, process it and save it
     * @param numTickets Number of tickets being bought
     * @param eventTitle Title of event being booked
     * @param studentEmail Email of student
     * @param studentPhone Phone number of student
     * @param epEmail Email of entertainment provider
     * @param transactionAmount Amount to bill the purchaser for; total price of tickets.
     */
    @Override
    public boolean processPayment(int numTickets, String eventTitle, String studentEmail, 
        String studentPhone, String epEmail, double transactionAmount) {
        if (verifyInput(numTickets, transactionAmount)){
            this.bookings.add(new Booking(numTickets, eventTitle, studentEmail, 
                studentPhone, epEmail, transactionAmount));
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Given information for a single booking, check it and refund it
     * @param numTickets Number of tickets being bought
     * @param eventTitle Title of event being booked
     * @param studentEmail Email of student
     * @param studentPhone Phone number of student
     * @param epEmail Email of entertainment provider
     * @param transactionAmount Amount booking was charged for
     * @param organiserMsg Message from organiser to show to students, not processed here
     */

    @Override
    public boolean processRefund(int numTickets, String eventTitle, String studentEmail, 
        String studentPhone, String epEmail, double transactionAmount, String organiserMsg) {
        if (verifyInput(numTickets, transactionAmount)){
            int index = this.bookings.indexOf(new Booking(numTickets, eventTitle, studentEmail, 
                studentPhone, epEmail, transactionAmount));
            if (index != -1){
                this.bookings.remove(index);
                return true;
            } 
        }
        return false;
    }
    
}
