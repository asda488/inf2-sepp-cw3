package com.acmecorp.events;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ViewPerformanceSystemTests extends SystemTestsBase {

    @Test
    void viewPerformanceSuccess() {
        when(textUserInterfaceMock.getInput(contains("Menu")))
            .thenReturn("5", "1", "0");

        assertDoesNotThrow(() -> menuController.mainMenu());
    }

    @Test
    void viewPerformanceInvalidSelection() {
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("999", "0");

        assertDoesNotThrow(() -> menuController.mainMenu());
    }
}