package com.acmecorp;

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

public class LogoutSystemTests extends SystemTestsBase{
    static List<Arguments> ADMIN_LOGIN_CREDENTIALS = Arrays.asList(
        arguments("example@example.com","password"),
        arguments("topaz@example.com","123456"),
        arguments("peridot@hindeburgh.ac.uk","admin")
    );

    static List<Arguments> STUDENT_LOGIN_CREDENTIALS = Arrays.asList(
        arguments("john@hindeburgh.ac.uk","hunter1"),
        arguments("jane@hindeburgh.ac.uk","abc123"),
        arguments("james@hindeburgh.ac.uk","mypassword")
    );

    void attemptLoginLogoutAndExit(String email, String password){
        //attempt login and then logout with mocked inputs
        //while on a menu, select the login in action, then logout, then exit
        when(this.textUserInterfaceMock.getInput(Mockito.contains("Menu"))).thenReturn("1", "1", "0"); 
        when(this.textUserInterfaceMock.getInput(Mockito.contains("email"))).thenReturn(email); //fill in email
        when(this.textUserInterfaceMock.getInput(Mockito.contains("password"))).thenReturn(password); //fill in password
        this.menuController.mainMenu(); 
    }


    @ParameterizedTest
    @FieldSource("STUDENT_LOGIN_CREDENTIALS")
    void logoutStudentSuccess(String email, String password){
        attemptLoginLogoutAndExit(email, password);
        verify(this.textUserInterfaceMock, times(1)
            .description("Student logout was unsuccesful."))
            .displaySuccess(String.format("Successfully logged out.", email));
    }

    @ParameterizedTest
    @FieldSource("ADMIN_LOGIN_CREDENTIALS")
    void logoutAdminSuccess(String email, String password){
        attemptLoginLogoutAndExit(email, password);
        verify(this.textUserInterfaceMock, times(1)
            .description("Admin logout was unsuccesful."))
            .displaySuccess(String.format("Successfully logged out.", email));
    }
}