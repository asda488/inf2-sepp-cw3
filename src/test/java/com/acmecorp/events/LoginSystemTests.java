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

public class LoginSystemTests extends SystemTestsBase {
    
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

    static List<Arguments> INVALID_EMAIL_LOGIN_CREDENTIALS = Arrays.asList(
        arguments("jack@hindeburgh.ac.uk","jacksworld"),
        arguments("jane@gmail.com","abcdef"),
        arguments("malicious","malicious")
    );

    static List<Arguments> INVALID_PASSWORD_LOGIN_CREDENTIALS = Arrays.asList(
        arguments("john@hindeburgh.ac.uk","hunter2"),
        arguments("peridot@hindeburgh.ac.uk","letmein"),
        arguments("topaz@example.com","malicious")
    );
    
    /**
     * Attempt login with mocked inputs
     */
    void attemptLoginAndExit(String email, String password){
        //while on a menu, select the login in action, and then exit the next time
        when(this.textUserInterfaceMock.getInput(Mockito.contains("Menu"))).thenReturn("1").thenReturn("0"); 
        when(this.textUserInterfaceMock.getInput(Mockito.contains("email"))).thenReturn(email); //fill in email
        when(this.textUserInterfaceMock.getInput(Mockito.contains("password"))).thenReturn(password); //fill in password
        this.menuController.mainMenu(); 
    }

    /** Check students can log in succesfully */
    @ParameterizedTest
    @FieldSource("STUDENT_LOGIN_CREDENTIALS")
    void loginStudentSuccess(String email, String password){
        attemptLoginAndExit(email, password);
        verify(this.textUserInterfaceMock, times(1)
            .description("Student login was unsuccessful even though credentials were correct."))
            .displaySuccess(String.format("Logged in with email %s.", email));
    }

    /** Check admins can log in succesfully */
    @ParameterizedTest
    @FieldSource("ADMIN_LOGIN_CREDENTIALS")
    void loginAdminSuccess(String email, String password){
        attemptLoginAndExit(email, password);
        verify(this.textUserInterfaceMock, times(1)
            .description("Admin login was unsuccessful even though credentials were correct."))
            .displaySuccess(String.format("Logged in with email %s.", email));
    }

    /** Check that logging in with the wrong email yields the correct error */
    @ParameterizedTest
    @FieldSource("INVALID_EMAIL_LOGIN_CREDENTIALS")
    void loginInvalidEmailFail(String email, String password){
        attemptLoginAndExit(email, password);
        verify(this.textUserInterfaceMock, times(1)
            .description("Student login was successful even though credentials were incorrect."))
            .displayError("Account not found.");
    }

    /** Check that logging in with the wrong password yields the correct error */
    @ParameterizedTest
    @FieldSource("INVALID_PASSWORD_LOGIN_CREDENTIALS")
    void loginInvalidPasswordFail(String email, String password){
        attemptLoginAndExit(email, password);
        verify(this.textUserInterfaceMock, times(1)
            .description("Student login was successful even though password was incorrect."))
            .displayError("Password incorrect.");
    }

    //TODO:expand to EP
}
