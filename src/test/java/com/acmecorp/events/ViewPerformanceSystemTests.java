package com.acmecorp.events;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ViewPerformanceSystemTests extends SystemTestsBase {

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