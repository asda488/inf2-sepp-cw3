package com.acmecorp.events;

import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;

import com.acmecorp.events.Controllers.BookingController;
import com.acmecorp.events.Controllers.EventPerformanceController;
import com.acmecorp.events.Controllers.MenuController;
import com.acmecorp.events.Controllers.UserController;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Services.MockVerificationSystem;
import com.acmecorp.events.Services.MockPaymentSystem;

import com.acmecorp.events.Views.TextUserInterface;

public class SystemTestsBase {
    TextUserInterface textUserInterfaceMock;
    User currentUserMock;

    UserController userController;
    BookingController bookingController;
    EventPerformanceController eventPerformanceController;
    MenuController menuController;

    @BeforeEach
    void setUp(){
        //setup mock textUserInterface
        this.textUserInterfaceMock = mock(TextUserInterface.class);      

        //setup sub controllers
        User user = null;
        this.userController = new UserController(textUserInterfaceMock, new MockVerificationSystem(), user);
        this.bookingController = new BookingController(textUserInterfaceMock, user);
        this.eventPerformanceController = new EventPerformanceController(textUserInterfaceMock, user, new MockPaymentSystem());

        //setup main controller
        this.menuController = new MenuController(userController, bookingController, 
            eventPerformanceController, textUserInterfaceMock, user);
    }
}
