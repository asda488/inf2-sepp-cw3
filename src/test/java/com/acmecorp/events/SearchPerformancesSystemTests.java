package com.acmecorp.events;

import com.acmecorp.events.Controllers.EventPerformanceController;
import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class SearchPerformancesSystemTests extends SystemTestsBase {

    @BeforeEach
    void setUpPerformance() {
        User ep = new EntertainmentProvider("ep@example.com", "password", "123456", "A music promoter", "John Smith", "Acme Events");
        this.eventPerformanceController = new EventPerformanceController(textUserInterfaceMock, ep);
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
    void shouldSearchPerformancesSuccessfully() {
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("2026-04-18");

        eventPerformanceController.searchForPerformances();

        verify(textUserInterfaceMock, never()).displayError(anyString());
        verify(textUserInterfaceMock, times(1)).displayListofPerformances(any());
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