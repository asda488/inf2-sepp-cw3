package com.acmecorp;

public class Main {
    /**
     * Application entry point
     */
    public static void main() {
        //setup drivers 
        TextUserInterface textUserInterface = new TextUserInterface();
        MockVerificationSystem mockVerificationSystem = new MockVerificationSystem();

        //setup and run main controller
        MenuController menuController = new MenuController(textUserInterface, mockVerificationSystem);
        menuController.mainMenu();
    }
}
