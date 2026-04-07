package com.acmecorp.events.Controllers;

import java.util.Collection;
import java.util.EnumSet;

import com.acmecorp.events.Models.User;
import com.acmecorp.events.Views.View;

public class MenuController extends Controller {

    private final UserController userController;
    private final BookingController bookingController;
    private final EventPerformanceController eventPerformanceController;

    enum GuestMenuOptions {
        LOGIN,
        REGISTER_EP
    }

    enum StudentMenuOptions {
        LOGOUT,
        SEARCH_FOR_PERFORMANCES,
        VIEW_PERFORMANCE,
        REVIEW_PERFORMANCE,
        EDIT_PREFERENCES,
        BOOK_EVENT,
        CANCEL_BOOKING
    }

    enum EPMenuOptions {
        LOGOUT,
        SEARCH_FOR_PERFORMANCES,
        VIEW_PERFORMANCE,
        CREATE_EVENT,
        CANCEL_PERFORMANCE
    }

    enum AdminMenuOptions {
        LOGOUT,
        SEARCH_FOR_PERFORMANCES,
        VIEW_PERFORMANCE,
        SPONSOR_PERFORMANCE
    }

    public MenuController(UserController userController, BookingController bookingController,
        EventPerformanceController eventPerformanceController, View view, User currentUser) {
        super(view, null);
        this.currentUser = currentUser;

        //set sub-controllers
        this.userController = userController;
        this.bookingController = bookingController;
        this.eventPerformanceController = eventPerformanceController;
    }

    /**
     * Function to parse and display a EnumSet menu and get the user's input.
     * Loops until a valid response is given. Returns an int which corresponds to
     * the index of the constant (menu option)'s index in the enum passed.
     * @param menu Ordered collection (ideally EnumSet of enum) of menu options.
     * @return Integer corresponding to chosen option from input menu enum.
     */
    @Override
    public <T> int selectFromMenu(Collection<T> menu, String item){
        int inputNumber = -1; //initalise the inputNumber as -1 to start the loop
        while (!(inputNumber >= 0 && inputNumber <= menu.size())){
            //pretty print with counter
            String fullMenu = "Menu:\n";
            int i = 1;
            for (T t : menu){
                String menuOption = t.toString().replace("_", " ");
                fullMenu = fullMenu.concat(String.format("%d. %s\n", i, menuOption));
                i++;
            }
            //get input and return
            String input = this.view.getInput(
                String.format("%sPlease enter the number corresponding to the action you wish to take, or enter 0 to exit", fullMenu));
            try {
                inputNumber = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                this.view.displayError(
                    String.format("Input not recognised, please enter a number from 0 to %d.", menu.size())
                );
            }
        }
        return inputNumber - 1;
    }

    /**
     * Action called from mainMenu loop to synchronise userController.currentUser across all controllers.
     */
    private void synchroniseUserFromUserController(){
        this.currentUser = userController.currentUser;
        this.bookingController.setCurrentUser(this.currentUser);
        this.eventPerformanceController.setCurrentUser(this.currentUser);
    }

    /**
     * Main application loop of the program, runs until exit is requested.
     */
    public void mainMenu(){
        boolean run = true;
        int selectionIndex = -1;
        //main loop
        while (run){
            //set and display correct menu, and then switch over menu to run correct action
            if (this.checkCurrentUserIsStudent()){
                selectionIndex = this.selectFromMenu(EnumSet.allOf(StudentMenuOptions.class), "");
                if (selectionIndex != -1){
                    switch (StudentMenuOptions.values()[selectionIndex]){
                        case LOGOUT -> {
                            this.userController.logout();
                            this.synchroniseUserFromUserController();
                        }
                        case EDIT_PREFERENCES -> this.userController.editPreferences();
                        case BOOK_EVENT -> this.bookingController.bookPerformance();
                    }
                }
            } else if (this.checkCurrentUserIsEntertainmentProvider()){
                selectionIndex = this.selectFromMenu(EnumSet.allOf(EPMenuOptions.class), "");
                if (selectionIndex != -1){
                    switch (EPMenuOptions.values()[selectionIndex]){
                        case LOGOUT -> {
                            this.userController.logout();
                            this.synchroniseUserFromUserController();
                        }
                        case CANCEL_PERFORMANCE -> this.eventPerformanceController.cancelPerformance();
                    }
                }
            } else if (this.checkCurrentUserIsAdmin()){
                selectionIndex = this.selectFromMenu(EnumSet.allOf(AdminMenuOptions.class), "");
                if (selectionIndex != -1){
                    switch (AdminMenuOptions.values()[selectionIndex]){
                        case LOGOUT -> {
                            this.userController.logout();
                            this.synchroniseUserFromUserController();
                        }
                    }
                }
            } else if (this.checkCurrentUserIsGuest()){
                selectionIndex = this.selectFromMenu(EnumSet.allOf(GuestMenuOptions.class), "");
                if (selectionIndex != -1){
                    switch (GuestMenuOptions.values()[selectionIndex]){
                        case LOGIN -> {
                            this.userController.login();
                            this.synchroniseUserFromUserController();
                        }
                        case REGISTER_EP -> this.userController.registerEntertainmentProvider();
                    }
                }
            }
            //if exit was requested, set the run variable to false
            if (selectionIndex == -1){
                run = false;
            }
        }
    }
}
