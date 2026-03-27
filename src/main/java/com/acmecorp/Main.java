package com.acmecorp;

public class Main {
    public static void main() {
        TextUserInterface textUserInterface = new TextUserInterface();
        MenuController menuController = new MenuController(textUserInterface);
        menuController.mainMenu();
    }
}
