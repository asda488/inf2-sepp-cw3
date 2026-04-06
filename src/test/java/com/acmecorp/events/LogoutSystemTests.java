package com.acmecorp.events;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acmecorp.events.Models.AdminStaff;
import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;

public class LogoutSystemTests extends SystemTestsBase {

    static Stream<Arguments> admins() {
        return Stream.of(
            arguments(new AdminStaff("example@example.com", "password", "Example Example")),
            arguments(new AdminStaff("topaz@example.com", "123456", "Topaz Gemstone")),
            arguments(new AdminStaff("peridot@hindeburgh.ac.uk", "admin", "Peridot Gemstone"))
        );
    }

    static Stream<Arguments> students() {
        return Stream.of(
            arguments(new Student("john@hindeburgh.ac.uk", "hunter1", "John Smith", "07455753486")),
            arguments(new Student("jane@hindeburgh.ac.uk", "abc123", "Jane Smith", "07401176330")),
            arguments(new Student("james@hindeburgh.ac.uk", "mypassword", "James Stuart", "07403937357"))
        );
    }

    static Stream<Arguments> eps() {
        return Stream.of(
            arguments(new EntertainmentProvider(
                "admin@edinburghtheatre.com",
                "editheatre",
                "edi12345",
                "Edinburgh Theatres and Performing Arts",
                "Liam Theatremaster",
                "Edinburgh Theatres"
            )),
            arguments(new EntertainmentProvider(
                "circus@londoncircus.com",
                "f!nest",
                "lon98765",
                "London's finest touring circus",
                "Mr Clown",
                "London Circus"
            )),
            arguments(new EntertainmentProvider(
                "sales@comart.org.uk",
                "comart57",
                "sp24680a",
                "Official Community Art Centre",
                "Polly K",
                "Community Art Centre"
            ))
        );
    }

    /**
     * Configure controllers to begin as certain user, then attempt logout with certain inputs
     */
    void loginAsUserThenLogoutAndExit(User user) {
        this.userController.setCurrentUser(user);
        this.menuController.setCurrentUser(user);

        when(this.textUserInterfaceMock.getInput(Mockito.contains("Menu")))
            .thenReturn("1", "0");

        this.menuController.mainMenu();
    }

    /** Check that students can successfully log out */
    @ParameterizedTest
    @MethodSource("students")
    void logoutStudentSuccess(Student student) {
        loginAsUserThenLogoutAndExit(student);

        verify(this.textUserInterfaceMock, times(1)
            .description("Student logout was unsuccessful."))
            .displaySuccess("Successfully logged out.");
    }

    /** Check that admins can successfully log out */
    @ParameterizedTest
    @MethodSource("admins")
    void logoutAdminSuccess(AdminStaff adminStaff) {
        loginAsUserThenLogoutAndExit(adminStaff);

        verify(this.textUserInterfaceMock, times(1)
            .description("Admin logout was unsuccessful."))
            .displaySuccess("Successfully logged out.");
    }

    /** Check that EPs can successfully log out */
    @ParameterizedTest
    @MethodSource("eps")
    void logoutEntertainmentProviderSuccess(EntertainmentProvider entertainmentProvider) {
        loginAsUserThenLogoutAndExit(entertainmentProvider);

        verify(this.textUserInterfaceMock, times(1)
            .description("EP logout was unsuccessful."))
            .displaySuccess("Successfully logged out.");
    }
}