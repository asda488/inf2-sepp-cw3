package com.acmecorp.events.Models;

import java.util.Objects;

public class Booking {

    private String performanceName;
    private String studentEmail;

    public Booking(String performanceName) {
        this.performanceName = performanceName;
        this.studentEmail = "";
    }

    public Booking(String performanceName, String studentEmail) {
        this.performanceName = performanceName;
        this.studentEmail = studentEmail;
    }

    public String getPerformanceName() {
        return performanceName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return Objects.equals(performanceName, booking.performanceName) &&
               Objects.equals(studentEmail, booking.studentEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(performanceName, studentEmail);
    }
}