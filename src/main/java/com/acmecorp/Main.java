package com.acmecorp;

public class Main {
    /**
     * Application entry point
     */
    public static void main() {
        TextUserInterface textUserInterface = new TextUserInterface();
        MockVerificationSystem mockVerificationSystem = new MockVerificationSystem();
        MenuController menuController = new MenuController(textUserInterface, mockVerificationSystem);
        menuController.mainMenu();
    }
}
