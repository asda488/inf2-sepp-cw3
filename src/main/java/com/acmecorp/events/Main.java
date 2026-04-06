package com.acmecorp.events;

import com.acmecorp.events.Controllers.BookingController;
import com.acmecorp.events.Controllers.EventPerformanceController;
import com.acmecorp.events.Controllers.MenuController;
import com.acmecorp.events.Controllers.UserController;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Services.MockVerificationSystem;
import com.acmecorp.events.Views.TextUserInterface;

public class Main {

    /**
     * Application entry point
     */
    public static void main(String[] args) {

        TextUserInterface textUserInterface = new TextUserInterface();
        MockVerificationSystem mockVerificationSystem = new MockVerificationSystem();

        User user = null;

        UserController userController = new UserController(
            textUserInterface,
            mockVerificationSystem,
            user
        );

        BookingController bookingController = new BookingController(
            textUserInterface,
            user
        );

        EventPerformanceController eventPerformanceController = new EventPerformanceController(
            textUserInterface,
            user
        );

        MenuController menuController = new MenuController(
            userController,
            bookingController,
            eventPerformanceController,
            textUserInterface,
            user
        );

        // ❌ REMOVE this (MenuController already handles syncing)
        // bookingController.setCurrentUser(userController.getCurrentUser());
        // eventPerformanceController.setCurrentUser(userController.getCurrentUser());

        menuController.mainMenu();
    }
}