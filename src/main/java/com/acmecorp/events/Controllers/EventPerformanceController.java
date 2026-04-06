package com.acmecorp.events.Controllers;

import com.acmecorp.events.Models.Review;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Views.View;

public class EventPerformanceController extends Controller {

    public EventPerformanceController(View view, User currentUser) {
        super(view, currentUser);
    }

    public void reviewPerformance() {

        if (!(currentUser instanceof Student)) {
            view.displayError("Only students can leave reviews.");
            return;
        }
    
        Student student = (Student) currentUser;
    
        String performance = view.getInput("Enter performance name:");
        if (performance == null || performance.trim().isEmpty()) {
            view.displayError("Performance name cannot be empty.");
            return;
        }
    
        String ratingStr = view.getInput("Enter rating (1-5):");
        if (ratingStr == null) {
            view.displayError("Invalid rating.");
            return;
        }
    
        String comment = view.getInput("Enter comment:");
        if (comment == null) {
            view.displayError("Invalid comment.");
            return;
        }
    
        int rating;
        try {
            rating = Integer.parseInt(ratingStr.trim());
        } catch (NumberFormatException e) {
            view.displayError("Invalid rating.");
            return;
        }
    
        if (rating < 1 || rating > 5) {
            view.displayError("Rating must be between 1 and 5.");
            return;
        }
    
        Review review = new Review(performance.trim(), student.getEmail(), rating, comment);
    
        student.addReview(review);
    
        view.displaySuccess("Review submitted.");
    }

    public void sponsorPerformance() {

        if (!checkCurrentUserIsAdmin()) {
            view.displayError("Only admins can sponsor performances.");
            return;
        }

        String performance = view.getInput("Enter performance name to sponsor:");

        if (performance == null || performance.trim().isEmpty()) {
            view.displayError("Performance name cannot be empty.");
            return;
        }

        view.displaySuccess("Performance sponsored successfully.");
    }
}