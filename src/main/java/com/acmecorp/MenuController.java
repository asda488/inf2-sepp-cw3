package com.acmecorp;

import java.util.Collection;
import java.util.EnumSet;

public class MenuController extends Controller {

    private UserController userController;
    private BookingController bookingController;
    private EventPerformanceController eventPerformanceController;

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

    public MenuController(TextUserInterface textUserInterface) {
        super(textUserInterface, null);

        //initialise sub-controllers
        this.userController = new UserController(textUserInterface, this.currentUser);
        this.bookingController = new BookingController(textUserInterface, this.currentUser);
        this.eventPerformanceController = new EventPerformanceController(textUserInterface, this.currentUser);
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
        while (!(inputNumber >= 1 && inputNumber <= menu.size())){
            //pretty print with counter
            String fullMenu = "";
            int i = 1;
            for (T t : menu){
                String menuOption = t.toString().replace("_", " ");
                fullMenu = fullMenu.concat(String.format("\n%d. %s", i, menuOption));
                i++;
            }
            //get input and return
            String input = this.textUserInterface.getInput(
                String.format("%s\nPlease enter the number corresponding to the action you wish to take", fullMenu));
            inputNumber = Integer.parseInt(input);
        }
        return inputNumber - 1;
    }

    public void mainMenu(){
        //main loop
        while (true){
            //set and display correct menu, and then switch over menu to run correct command
            if (this.checkCurrentUserIsStudent()){
                switch (StudentMenuOptions.values()[this.selectFromMenu(EnumSet.allOf(StudentMenuOptions.class), "")]){
                    case LOGOUT -> {
                        this.userController.logout();
                        this.currentUser = userController.currentUser;
                        this.bookingController.currentUser = this.currentUser;
                        this.eventPerformanceController.currentUser = this.currentUser;
                    }
                }
            } else if (this.checkCurrentUserIsEntertainmentProvider()){
                switch (EPMenuOptions.values()[this.selectFromMenu(EnumSet.allOf(EPMenuOptions.class), "")]){
                }
            } else if (this.checkCurrentUserIsAdmin()){
                switch (AdminMenuOptions.values()[this.selectFromMenu(EnumSet.allOf(AdminMenuOptions.class), "")]){
                }
            } else if (this.checkCurrentUserIsGuest()){
                switch (GuestMenuOptions.values()[this.selectFromMenu(EnumSet.allOf(GuestMenuOptions.class), "")]){
                    case LOGIN -> {
                            this.userController.login();
                        this.currentUser = userController.currentUser;
                        this.bookingController.currentUser = this.currentUser;
                        this.eventPerformanceController.currentUser = this.currentUser;
                    }
                    case REGISTER_EP -> this.userController.registerEntertainmentProvider();
                }
            }  
        }
    }
}
