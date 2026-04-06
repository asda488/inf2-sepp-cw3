package com.acmecorp.events;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class SearchPerformancesSystemTests extends SystemTestsBase {

    @Test
    void shouldSearchPerformancesSuccessfully() {
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("2026-04-18");

        eventPerformanceController.searchForPerformances();

        verify(textUserInterfaceMock, never()).displayError(anyString());
        verify(textUserInterfaceMock, times(1)).displayListOfPerformances(any());
    }

    @Test
    void shouldRejectInvalidSearchDate() {
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("not-a-date") // invalid
            .thenReturn("2026-04-18"); // valid

        eventPerformanceController.searchForPerformances();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid date, try again.");
    }

    @Test
    void shouldRejectPastSearchDate() {
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("2020-01-01")    // invalid - past date
            .thenReturn("2026-04-18");   // valid

        eventPerformanceController.searchForPerformances();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid date, try again.");
    }

    @Test
    void shouldDisplayMessageWhenNoPerformancesFound() {
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("2026-12-31"); // valid date but no performances

        eventPerformanceController.searchForPerformances();

        verify(textUserInterfaceMock, times(1))
            .displayError("There are no performances on 2026-12-31");
    }
}