package com.acmecorp.events.Controllers;

import java.util.List;

import com.acmecorp.events.Models.Booking;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Views.View;

public class BookingController extends Controller {
    
    public BookingController(View view, User currentUser) {
        super(view, currentUser);
    }

    public void cancelBooking() {

        if (!(currentUser instanceof Student)) {
            view.displayError("Only students can cancel bookings.");
            return;
        }

        Student student = (Student) currentUser;
        List<Booking> bookings = student.getBookings();

        if (bookings == null || bookings.isEmpty()) {
            view.displayError("No bookings found.");
            return;
        }

        StringBuilder message = new StringBuilder("Your bookings:\n");
        for (int i = 0; i < bookings.size(); i++) {
            message.append(i + 1)
                   .append(". ")
                   .append(bookings.get(i).getPerformanceName())
                   .append("\n");
        }

        String input = view.getInput(
            message + "Please enter the number corresponding to the booking you wish to cancel:"
        );

        if (input == null) {
            view.displayError("Invalid input.");
            return;
        }

        int index;
        try {
            index = Integer.parseInt(input.trim()) - 1;
        } catch (NumberFormatException e) {
            view.displayError("Invalid input.");
            return;
        }

        if (index < 0 || index >= bookings.size()) {
            view.displayError("Invalid selection.");
            return;
        }

        Booking booking = bookings.get(index);
        student.removeBooking(booking);

        view.displaySuccess("Booking cancelled successfully.");
    }

    public void bookEvent() {

        if (!(currentUser instanceof Student)) {
            view.displayError("Only students can book events.");
            return;
        }

        Student student = (Student) currentUser;

        String performance = view.getInput("Please enter performance name:");

        if (performance == null) {
            view.displayError("Invalid input.");
            return;
        }

        String trimmedPerformance = performance.trim();

        if (trimmedPerformance.isEmpty()) {
            view.displayError("Invalid input.");
            return;
        }

        Booking booking = new Booking(trimmedPerformance, student.getEmail());

        student.addBooking(booking);

        view.displaySuccess("Booking successful.");
    }
}