package com.acmecorp.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acmecorp.events.Controllers.BookingController;
import com.acmecorp.events.Controllers.EventPerformanceController;
import com.acmecorp.events.Controllers.MenuController;
import com.acmecorp.events.Controllers.UserController;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Services.MockPaymentSystem;
import com.acmecorp.events.Services.MockVerificationSystem;
import com.acmecorp.events.Views.TextUserInterface;

public class EditPreferencesSystemTests {
    private record Setup(TextUserInterface view, UserController uc,
        BookingController bc, EventPerformanceController ec,
        MenuController mc) {}

    private Setup make() {
        TextUserInterface view = mock(TextUserInterface.class);
        User user = null;
        UserController uc = new UserController(view, new MockVerificationSystem(),
            user);
        BookingController bc = new BookingController(view, user,
            new java.util.ArrayList<>(),
            new MockPaymentSystem(new java.util.ArrayList<>()));
        EventPerformanceController ec = new EventPerformanceController(view, user,
            new java.util.ArrayList<>(),
            new MockPaymentSystem(new java.util.ArrayList<>()));
        MenuController mc = new MenuController(uc, bc, ec, view, user);
        return new Setup(view, uc, bc, ec, mc);
    }

    private void setStudent(Student s, Setup setup) {
        setup.uc().setCurrentUser(s);
        setup.bc().setCurrentUser(s);
        setup.ec().setCurrentUser(s);
        setup.mc().setCurrentUser(s);
    }

    @Test
    void editPrefsSuccessTest() {
        Setup setup = make();
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        setStudent(s, setup);

        when(setup.view().getInput(contains("Menu"))).thenReturn("5", "0");
        when(setup.view().getInput(contains("comma separated")))
            .thenReturn("music, theatre");

        setup.mc().mainMenu();

        assertTrue(s.getPreferences().isPreferMusicEvents(),
            "Music preference was not updated.");
        assertTrue(s.getPreferences().isPreferTheatreEvents(),
            "Theatre preference was not updated.");
        verify(setup.view(), times(1))
            .displaySuccess("Preferences successfully updated.");
    }

    @Test
    void editPrefsInvalidThenSuccessTest() {
        Setup setup = make();
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        setStudent(s, setup);

        when(setup.view().getInput(contains("Menu"))).thenReturn("5", "0");
        when(setup.view().getInput(contains("comma separated")))
            .thenReturn("bad", "sports");

        setup.mc().mainMenu();

        verify(setup.view(), times(1))
            .displayError("One or more preferences were invalid.");
        assertTrue(s.getPreferences().isPreferSportsEvents(),
            "Sports preference was not updated after retry.");
    }

    @Test
    void editPrefsBlankClearsTest() {
        Setup setup = make();
        Student s = new Student("john@hindeburgh.ac.uk", "hunter1",
            "John Smith", "07455753486");
        s.getPreferences().updatePreferences("music, sports");
        setStudent(s, setup);

        when(setup.view().getInput(contains("Menu"))).thenReturn("5", "0");
        when(setup.view().getInput(contains("comma separated")))
            .thenReturn("   ");

        setup.mc().mainMenu();

        assertFalse(s.getPreferences().isPreferMusicEvents(),
            "Blank input did not clear music.");
        assertFalse(s.getPreferences().isPreferSportsEvents(),
            "Blank input did not clear sports.");
    }
}
