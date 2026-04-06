package com.acmecorp.events;

import com.acmecorp.events.Models.Review;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewTests {

    @Test
    void testReviewCreation() {
        Review review = new Review("Concert", "test@test.com", 5, "Great!");

        assertAll(
            () -> assertEquals("Concert", review.getPerformance()),
            () -> assertEquals("test@test.com", review.getStudentEmail()),
            () -> assertEquals(5, review.getRating()),
            () -> assertEquals("Great!", review.getComment())
        );
    }
}