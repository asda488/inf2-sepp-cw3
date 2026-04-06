package com.acmecorp.events;

import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

import com.acmecorp.events.Controllers.BookingController;
import com.acmecorp.events.Controllers.EventPerformanceController;
import com.acmecorp.events.Controllers.MenuController;
import com.acmecorp.events.Controllers.UserController;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Services.MockVerificationSystem;
import com.acmecorp.events.Views.TextUserInterface;

public class SystemTestsBase {

    protected TextUserInterface textUserInterfaceMock;

    protected UserController userController;
    protected BookingController bookingController;
    protected EventPerformanceController eventPerformanceController;
    protected MenuController menuController;

    @BeforeEach
    void setUp() {

        this.textUserInterfaceMock = mock(TextUserInterface.class);

        User user = null;

        this.userController = new UserController(
            textUserInterfaceMock,
            new MockVerificationSystem(),
            user
        );

        this.bookingController = new BookingController(
            textUserInterfaceMock,
            user
        );

        this.eventPerformanceController = new EventPerformanceController(
            textUserInterfaceMock,
            user
        );

        this.menuController = new MenuController(
            userController,
            bookingController,
            eventPerformanceController,
            textUserInterfaceMock,
            user
        );
    }
}