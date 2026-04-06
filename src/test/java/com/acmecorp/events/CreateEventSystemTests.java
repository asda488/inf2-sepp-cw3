package com.acmecorp.events;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateEventSystemTests extends SystemTestsBase {

    @Test
    void shouldCreateEventSuccessfully() {
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

        Event event = eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, never()).displayError(anyString());

        assertNotNull(event);
    }

    @Test
    void shouldRejectNonEntertainmentProviderUser() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(false);

        Event event = eventPerformanceController.createEvent();

        assertNull(event);
        verify(textUserInterfaceMock, times(1))
            .displayError("Only EPs can create events");
        verify(textUserInterfaceMock, never())
            .getInput(anyString());
    }

    @Test
    void shouldRejectEmptyEventTitle() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("") // invalid
            .thenReturn("Scotland vs England Fan Zone") // valid
            .thenReturn("Sports")
            .thenReturn("no")
            .thenReturn("1")
            .thenReturn("2026-04-20T12:00")
            .thenReturn("2026-04-20T18:00")
            .thenReturn("Scotland")
            .thenReturn("The Beer Garden")
            .thenReturn("500")
            .thenReturn("yes")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid input, try again.");
    }

    @Test
    void shouldRejectInvalidEventType() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Film Society Premiere") 
            .thenReturn("Screening") // invalid
            .thenReturn("Movie") // valid
            .thenReturn("yes")
            .thenReturn("1")
            .thenReturn("2026-04-22T17:00")
            .thenReturn("2026-04-22T19:30")
            .thenReturn("Film Society")
            .thenReturn("5")
            .thenReturn("60")
            .thenReturn("Cinema Room")
            .thenReturn("80")
            .thenReturn("no")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid type, try again.");
    }

    @Test
    void shouldRejectNoOfPerformances() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Pantomime") 
            .thenReturn("Theatre")
            .thenReturn("yes")
            .thenReturn("0") // invalid
            .thenReturn("1") //valid
            .thenReturn("2026-04-25T17:00")
            .thenReturn("2026-04-25T21:30")
            .thenReturn("Acting School")
            .thenReturn("20")
            .thenReturn("300")
            .thenReturn("Big Hall")
            .thenReturn("400")
            .thenReturn("no")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid number, try again.");
    }

    @Test
    void shouldRejectInvalidDate() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Dance Showcase") 
            .thenReturn("Dance")
            .thenReturn("no")
            .thenReturn("1")
            .thenReturn("2026-04-27T96:00") // invalid
            .thenReturn("2026-04-27T99:75") // invalid
            .thenReturn("2026-04-27T16:00") // valid
            .thenReturn("2026-04-27T19:45") // valid
            .thenReturn("Dance School")
            .thenReturn("Small Hall")
            .thenReturn("120")
            .thenReturn("no")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(2))
            .displayError("Invalid input, try again.");
    }

    @Test
    void shouldRejectEndBeforeStart() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Auditions") 
            .thenReturn("Theatre")
            .thenReturn("no")
            .thenReturn("1")
            .thenReturn("2026-04-27T12:00") // invalid
            .thenReturn("2026-04-27T06:00") // invalid
            .thenReturn("2026-04-27T12:00") // valid
            .thenReturn("2026-04-27T18:00") // valid
            .thenReturn("Arts Center")
            .thenReturn("Small Hall")
            .thenReturn("120")
            .thenReturn("no")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid end time, try again.");
    }

    @Test
    void shouldRejectOverlappingPerformances() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Premier League") 
            .thenReturn("Sports")
            .thenReturn("no")
            .thenReturn("2")
            .thenReturn("2026-04-28T15:00")
            .thenReturn("2026-04-28T16:45")
            .thenReturn("Sky Sports")
            .thenReturn("Blue Room")
            .thenReturn("100")
            .thenReturn("no")
            .thenReturn("no")
            .thenReturn("2026-04-28T16:30") // invalid
            .thenReturn("2026-04-28T18:15") // invalid
            .thenReturn("2026-04-28T17:30") // valid
            .thenReturn("2026-04-28T19:15") // valid
            .thenReturn("TNT Sports")
            .thenReturn("Red Room")
            .thenReturn("100")
            .thenReturn("no")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Overlapping performance times, try again.");
    }

    @Test
    void shouldRejectPerformerNames() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Edinburgh Orchestra Charity Fundraiser") 
            .thenReturn("Music")
            .thenReturn("yes")
            .thenReturn("1")
            .thenReturn("2026-04-30T17:00")
            .thenReturn("2026-04-30T21:30")
            .thenReturn("") // invalid
            .thenReturn("UofE Orchestra, ENU Orchestra, QMU Orchestra, HW Orchestra") // valid
            .thenReturn("5")
            .thenReturn("300")
            .thenReturn("Big Hall")
            .thenReturn("400")
            .thenReturn("no")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid string, try again.");
    }

    @Test
    void shouldRejectTicketPrice() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Singing in the Rain") 
            .thenReturn("Theatre")
            .thenReturn("yes")
            .thenReturn("1")
            .thenReturn("2026-05-01T18:00")
            .thenReturn("2026-05-01T22:00")
            .thenReturn("Theatre Society")
            .thenReturn("15.0.0") // invalid
            .thenReturn("15") // valid
            .thenReturn("400")
            .thenReturn("Large Theatre")
            .thenReturn("600")
            .thenReturn("no")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid price, try again.");
    }

    @Test
    void shouldRejectNoOfTickets() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Classic Film Screening") 
            .thenReturn("Movie")
            .thenReturn("yes")
            .thenReturn("1")
            .thenReturn("2026-05-02T17:00")
            .thenReturn("2026-05-02T21:30")
            .thenReturn("Film Society")
            .thenReturn("5")
            .thenReturn("-100") // invalid
            .thenReturn("80") // valid
            .thenReturn("Lecture Hall")
            .thenReturn("150")
            .thenReturn("no")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid number, try again.");
    }

    @Test
    void shouldRejectVenueAddress() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Varsity Game") 
            .thenReturn("Sports")
            .thenReturn("no")
            .thenReturn("1")
            .thenReturn("2026-05-04T15:00")
            .thenReturn("2026-05-04T17:00")
            .thenReturn("University of Edinburgh, University of St Andrews")
            .thenReturn("") // invalid
            .thenReturn("Peffermill") // valid
            .thenReturn("400")
            .thenReturn("yes")
            .thenReturn("yes");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid input, try again.");
    }

    @Test
    void shouldRejectVenueCapacity() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Dance Competition") 
            .thenReturn("Dance")
            .thenReturn("no")
            .thenReturn("1")
            .thenReturn("2026-05-07T16:00")
            .thenReturn("2026-05-07T22:00")
            .thenReturn("UofE Dance, ENU Dance, QMU Dance, HW Dance")
            .thenReturn("Big Hall")
            .thenReturn("Three hundred") // invalid
            .thenReturn("300") // valid
            .thenReturn("no")
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid number, try again.");
    }

    @Test
    void shouldRejectOutdoorOption() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("World Cup Fan Zone") 
            .thenReturn("Sports")
            .thenReturn("no")
            .thenReturn("1")
            .thenReturn("2026-06-15T12:00")
            .thenReturn("2026-06-15T21:00")
            .thenReturn("World Cup")
            .thenReturn("Temporary Tent")
            .thenReturn("1000")
            .thenReturn("nope") // invalid
            .thenReturn("no") // valid
            .thenReturn("no");

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid choice, try again.");
    }

    @Test
    void shouldRejectSmokingOption() {
        when(currentUserMock.checkCurrentUserIsEntertainmentProvider()).thenReturn(true);
        when(textUserInterfaceMock.getInput(anyString()))
            .thenReturn("Band World Tour") 
            .thenReturn("Music")
            .thenReturn("yes")
            .thenReturn("1")
            .thenReturn("2026-05-13T18:00")
            .thenReturn("2026-05-13T23:00")
            .thenReturn("Big Band")
            .thenReturn("30")
            .thenReturn("2000")
            .thenReturn("Field")
            .thenReturn("5000")
            .thenReturn("yes")
            .thenReturn("yeah") // invalid
            .thenReturn("yes"); // valid

        eventPerformanceController.createEvent();

        verify(textUserInterfaceMock, times(1))
            .displayError("Invalid choice, try again.");
    }
}