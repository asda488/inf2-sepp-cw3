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
    public static void main() {
        //setup drivers 
        TextUserInterface textUserInterface = new TextUserInterface();
        MockVerificationSystem mockVerificationSystem = new MockVerificationSystem();
        
        //setup sub controllers
        User user = null;
        UserController userController = new UserController(textUserInterface, mockVerificationSystem, user);
        BookingController bookingController = new BookingController(textUserInterface, user);
        EventPerformanceController eventPerformanceController = new EventPerformanceController(textUserInterface, user);

        //setup and run main controller
        MenuController menuController = new MenuController(userController, bookingController, 
            eventPerformanceController, textUserInterface, user);
        menuController.mainMenu();
    }
}
