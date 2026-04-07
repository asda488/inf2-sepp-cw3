package com.acmecorp.events.Models;

import java.time.LocalDateTime;

public class Booking {

    public static enum BookingStatus {
        ACTIVE,
        CANCELLED_BY_USER,
        CANCELLED_BY_PROVIDER,
        PAYMENT_FAILED
    }

    private long bookNo;
    private int numTix;
    private double paid;
    private LocalDateTime bookDt;
    private BookingStatus status;
    private Student stu;
    private Performance perf;

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
        this.paid = amountPaid;
        this.bookDt = bookingDate;
        this.bookNo = bookingNumber;
        this.numTix = numTickets;
        this.stu = student;
        this.status = BookingStatus.ACTIVE;
    }

    public Booking(long bookingNumber, int numTickets, double amountPaid,
        LocalDateTime bookingDate, Student student, Performance performance) {
        this(bookingNumber, numTickets, amountPaid, bookingDate, student);
        this.perf = performance;
    }

    public long getBookingNumber() {
        return bookNo;
    }

    public void setBookingNumber(long bookingNumber) {
        this.bookNo = bookingNumber;
    }

    public int getNumTickets() {
        return numTix;
    }

    public void setNumTickets(int numTickets) {
        this.numTix = numTickets;
    }

    public double getAmountPaid() {
        return paid;
    }

    public void setAmountPaid(double amountPaid) {
        this.paid = amountPaid;
    }

    public LocalDateTime getBookingDate() {
        return bookDt;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookDt = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setStudent(Student student) {
        this.stu = student;
    }

    public Student getStudent() {
        return stu;
    }

    public Performance getPerformance() {
        return perf;
    }

    public void setPerformance(Performance performance) {
        this.perf = performance;
    }

    public void cancelByStudent(){
        this.status = BookingStatus.CANCELLED_BY_USER;
    }

    public void cancelPaymentFailed(){
        this.status = BookingStatus.PAYMENT_FAILED;
    }

    public void cancelByProvider(){
        this.status = BookingStatus.CANCELLED_BY_PROVIDER;
    }

    public boolean checkBookedByStudent(String email){
        return this.stu.getEmail().equalsIgnoreCase(email);
    }

    public String getStudentDetails(){
        return "Student = " + stu.getName()
            + ", Email = " + stu.getEmail()
            + ", Phone = " + stu.getPhoneNumber();
    }

    public String generateBookingRecord(){
        String out = "Booking = " + bookNo
            + ", Tickets = " + numTix
            + ", Paid = " + paid
            + ", Date = " + bookDt
            + ", " + getStudentDetails();
        if (perf != null) {
            out += ", Event = " + perf.getEventTitle()
                + ", PerformanceID = " + perf.getPerformanceID();
        }
        return out;
    }
}
