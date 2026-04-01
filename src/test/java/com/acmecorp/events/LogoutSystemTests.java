package com.acmecorp.events;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.FieldSource;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acmecorp.events.Models.AdminStaff;
import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;

public class LogoutSystemTests extends SystemTestsBase{
    static List<Arguments> ADMINS = Arrays.asList(
        arguments(new AdminStaff("example@example.com","password", "Example Example")),
        arguments(new AdminStaff("topaz@example.com","123456", "Topaz Gemstone")),
        arguments(new AdminStaff("peridot@hindeburgh.ac.uk","admin", "Peridot Gemstone"))
    );

    static List<Arguments> STUDENTS = Arrays.asList(
        arguments(new Student("john@hindeburgh.ac.uk","hunter1", "John Smith", "07455753486")),
        arguments(new Student("jane@hindeburgh.ac.uk","abc123", "Jane Smith", "07401176330")),
        arguments(new Student("james@hindeburgh.ac.uk","mypassword", "James Stuart", "07403937357"))
    );

    static List<Arguments> EPS = Arrays.asList(
        arguments(new EntertainmentProvider("admin@edinburghtheatre.com", "editheatre", "edi12345", 
            "Edinburgh Theatres and Performing Arts", "Liam Theatremaster", "Edinburgh Theatres")),
        arguments(new EntertainmentProvider("circus@londoncircus.com", "f!nest", "lon98765",
            "London's finest touring circus", "Mr Clown", "London Circus")),
        arguments(new EntertainmentProvider("sales@comart.org.uk", "comart57", "sp24680a",
            "Official Community Art Centre", "Polly K", "Community Art Centre"))
    );

    /**
     * Configure controllers to begin as certain user, then attempt logout with certain inputs
     */
    void loginAsUserThenLogoutAndExit(User user){
        //login by direct function call
        this.userController.setCurrentUser(user);
        this.menuController.setCurrentUser(user);

        //then logout with mocked inputs; while on a menu logout, then exit
        when(this.textUserInterfaceMock.getInput(Mockito.contains("Menu"))).thenReturn("1", "0"); 
        this.menuController.mainMenu(); 
    }

    /** Check that students can successfully log out */
    @ParameterizedTest
    @FieldSource("STUDENTS")
    void logoutStudentSuccess(Student student){
        loginAsUserThenLogoutAndExit(student);
        verify(this.textUserInterfaceMock, times(1)
            .description("Student logout was unsuccessful."))
            .displaySuccess("Successfully logged out.");
    }

    /** Check that admins can successfully log out */
    @ParameterizedTest
    @FieldSource("ADMINS")
    void logoutAdminSuccess(AdminStaff adminStaff){
        loginAsUserThenLogoutAndExit(adminStaff);
        verify(this.textUserInterfaceMock, times(1)
            .description("Admin logout was unsuccessful."))
            .displaySuccess("Successfully logged out.");
    }

    /** Check that EPs can successfully log out */
    @ParameterizedTest
    @FieldSource("EPS")
    void logoutEntertainmentProviderSuccess(EntertainmentProvider entertainmentProvider){
        loginAsUserThenLogoutAndExit(entertainmentProvider);
        verify(this.textUserInterfaceMock, times(1)
            .description("EP logout was unsuccessful."))
            .displaySuccess("Successfully logged out.");
    }}