package com.acmecorp.events.Models;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    private String name;
    private String phoneNumber;
    private final List<Review> reviews = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    public Student(String email, String password, String name, String phoneNumber) {
        super(email, password);
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void addReview(Review review) {
        if (review != null) {
            reviews.add(review);
        }
    }

    public List<Review> getReviews() {
        return new ArrayList<>(reviews);
    }

    public void addBooking(Booking booking) {
        if (booking != null) {
            bookings.add(booking);
        }
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}