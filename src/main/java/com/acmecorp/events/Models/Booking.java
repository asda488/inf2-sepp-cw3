package com.acmecorp.events.Models;

import java.time.LocalDateTime;

public class Booking {

    public static enum BookingStatus {
        ACTIVE,
        CANCELLED_BY_USER,
        CANCELLED_BY_PROVIDER,
        PAYMENT_FAILED
    }

    private long bookingNumber;
    private int numTickets;
    private double amountPaid;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private Student student;

    /**
     * Model class representing one instance of a booking for
     * a specific perfomance, made at a time
     * @param amountPaid Amount paid for the booking
     * @param bookingDate Datetime that they booking was made
     * @param bookingNumber Unique ID assigned to the booking for identification
     * @param numTickets Number of tickets booked
     * @param student Student booking belongs to
     */
    public Booking(long bookingNumber, int numTickets, double amountPaid, 
        LocalDateTime bookingDate, Student student) {
        this.amountPaid = amountPaid;
        this.bookingDate = bookingDate;
        this.bookingNumber = bookingNumber;
        this.numTickets = numTickets;
        this.student = student;
        this.status = BookingStatus.ACTIVE;
    }

    public long getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(long bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setStudent(Student student) {
        this.student = student;
    }


    public void cancelByStudent(){

    }

    public void cancelPaymentFailed(){
        
    }

    public void cancelByProvider(){
        
    }
    public boolean checkBookedByStudent(String email){
        return false;
    }
    public String getStudentDetails(){
        return null;
    }
    public String generateBookingRecord(){
        return null;
    }
}
