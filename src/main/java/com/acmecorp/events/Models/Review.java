package com.acmecorp.events.Models;

public class Review {

    private final String performance;
    private final String studentEmail;
    private final int rating;
    private final String comment;

    public Review(String performance, String studentEmail, int rating, String comment) {
        this.performance = performance;
        this.studentEmail = studentEmail;
        this.rating = rating;
        this.comment = comment;
    }

    public String getPerformance() {
        return performance;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}