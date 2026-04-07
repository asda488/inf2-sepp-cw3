package com.acmecorp.events;

import com.acmecorp.events.Controllers.EventPerformanceController;
import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Services.MockPaymentSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ViewPerformanceSystemTests extends SystemTestsBase {

    @BeforeEach
    void setUpPerformance() {
        User ep = new EntertainmentProvider("ep@example.com", "password", "123456", "A music promoter", "John Smith", "Acme Events");
        this.eventPerformanceController = new EventPerformanceController(textUserInterfaceMock, ep, new MockPaymentSystem());
        when(textUserInterfaceMock.getInput(anyString()))
                .thenReturn("Rock Concert")
                .thenReturn("Music")
                .thenReturn("yes")
                .thenReturn("1")
                .thenReturn("2026-04-18T20:00")
                .thenReturn("2026-04-18T23:00")
                .thenReturn("John Smith")
                .thenReturn("9.99")
                .thenReturn("200")
                .thenReturn("The Pit")
                .thenReturn("500")
                .thenReturn("no")
                .thenReturn("yes");

        eventPerformanceController.createEvent();

        reset(textUserInterfaceMock); // clear the stub so individual tests start fresh
    }

    @Test
    void shouldViewPerformanceSuccessfully() {
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("0"); // valid ID - first performance

        eventPerformanceController.viewPerformance();

        verify(textUserInterfaceMock, never()).displayError(anyString());
        verify(textUserInterfaceMock, times(1)).displaySpecificPerformance(anyString());
        verify(textUserInterfaceMock, times(1)).displaySuccess(anyString());
    }

    @Test
    void shouldRejectInvalidPerformanceID() {
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("abc") // invalid
            .thenReturn("0"); // valid

        eventPerformanceController.viewPerformance();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid ID, try again.");
    }

    @Test
    void shouldDisplayMessageWhenPerformanceNotFound() {
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("9999"); // valid ID format but doesn't exist

        eventPerformanceController.viewPerformance();

        verify(textUserInterfaceMock, times(1))
            .displayError("No performance exists with ID 9999");
    }
}