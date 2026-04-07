package com.acmecorp.events.Models;

import java.util.ArrayList;
import java.util.List;

public class Student extends User{
    private String name;
    private String phoneNumber;
    private StudentPreferences pref;
    private List<Booking> bookList;

    /**
     * Constructor for student
     * @param email Email of student
     * @param password Plaintext password of student
     * @param name Full name of student
     * @param phoneNumber Phone number of student as an integer
     */
    public Student(String email, String password, String name, String phoneNumber) {
        super(email, password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pref = new StudentPreferences();
        this.bookList = new ArrayList<>();
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

    public StudentPreferences getPreferences() {
        return pref;
    }

    public void addBooking(Booking booking) {
        this.bookList.add(booking);
    }

    public List<Booking> getBookings() {
        return bookList;
    }
}
