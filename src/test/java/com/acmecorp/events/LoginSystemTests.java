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

public class LoginSystemTests extends SystemTestsBase {

    static Stream<Arguments> studentLoginCredentials() {
        return Stream.of(
            arguments("john@hindeburgh.ac.uk", "hunter1"),
            arguments("jane@hindeburgh.ac.uk", "abc123"),
            arguments("james@hindeburgh.ac.uk", "mypassword")
        );
    }

    static Stream<Arguments> adminLoginCredentials() {
        return Stream.of(
            arguments("example@example.com", "password"),
            arguments("topaz@example.com", "123456"),
            arguments("peridot@hindeburgh.ac.uk", "admin")
        );
    }

    static Stream<Arguments> invalidEmailLoginCredentials() {
        return Stream.of(
            arguments("jack@hindeburgh.ac.uk", "jacksworld"),
            arguments("jane@gmail.com", "abcdef"),
            arguments("malicious", "malicious")
        );
    }

    static Stream<Arguments> invalidPasswordLoginCredentials() {
        return Stream.of(
            arguments("john@hindeburgh.ac.uk", "hunter2"),
            arguments("peridot@hindeburgh.ac.uk", "letmein"),
            arguments("topaz@example.com", "malicious")
        );
    }

    /**
     * Attempt login with mocked inputs
     */
    void attemptLoginAndExit(String email, String password) {
        when(this.textUserInterfaceMock.getInput(Mockito.contains("Menu")))
            .thenReturn("1")
            .thenReturn("0");

        when(this.textUserInterfaceMock.getInput(Mockito.contains("email")))
            .thenReturn(email);

        when(this.textUserInterfaceMock.getInput(Mockito.contains("password")))
            .thenReturn(password);

        this.menuController.mainMenu();
    }

    /** Check students can log in successfully */
    @ParameterizedTest
    @MethodSource("studentLoginCredentials")
    void loginStudentSuccess(String email, String password) {
        attemptLoginAndExit(email, password);

        verify(this.textUserInterfaceMock, times(1)
            .description("Student login was unsuccessful even though credentials were correct."))
            .displaySuccess(String.format("Logged in with email %s.", email));
    }

    /** Check admins can log in successfully */
    @ParameterizedTest
    @MethodSource("adminLoginCredentials")
    void loginAdminSuccess(String email, String password) {
        attemptLoginAndExit(email, password);

        verify(this.textUserInterfaceMock, times(1)
            .description("Admin login was unsuccessful even though credentials were correct."))
            .displaySuccess(String.format("Logged in with email %s.", email));
    }

    /** Check that logging in with the wrong email yields the correct error */
    @ParameterizedTest
    @MethodSource("invalidEmailLoginCredentials")
    void loginInvalidEmailFail(String email, String password) {
        attemptLoginAndExit(email, password);

        verify(this.textUserInterfaceMock, times(1)
            .description("Login was successful even though email was incorrect."))
            .displayError("Account not found.");
    }

    /** Check that logging in with the wrong password yields the correct error */
    @ParameterizedTest
    @MethodSource("invalidPasswordLoginCredentials")
    void loginInvalidPasswordFail(String email, String password) {
        attemptLoginAndExit(email, password);

        verify(this.textUserInterfaceMock, times(1)
            .description("Login was successful even though password was incorrect."))
            .displayError("Password incorrect.");
    }
}