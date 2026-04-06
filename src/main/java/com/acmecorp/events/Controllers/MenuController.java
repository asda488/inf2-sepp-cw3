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

    public MenuController(UserController userController,
                          BookingController bookingController,
                          EventPerformanceController eventPerformanceController,
                          View view,
                          User currentUser) {
        super(view, currentUser);
        this.userController = userController;
        this.bookingController = bookingController;
        this.eventPerformanceController = eventPerformanceController;
    }

    @Override
    public <T> int selectFromMenu(Collection<T> menu, String item) {
        int inputNumber = -1;

        while (!(inputNumber >= 0 && inputNumber <= menu.size())) {

            StringBuilder fullMenu = new StringBuilder("Menu:\n");
            int i = 1;

            for (T t : menu) {
                String menuOption = t.toString().replace("_", " ");
                fullMenu.append(i)
                        .append(". ")
                        .append(menuOption)
                        .append("\n");
                i++;
            }

            String input = this.view.getInput(
                fullMenu + "Please enter the number corresponding to the action you wish to take, or enter 0 to exit"
            );

            if (input == null) {
                this.view.displayError(
                    String.format("Input not recognised, please enter a number from 0 to %d.", menu.size())
                );
                continue;
            }

            try {
                inputNumber = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                this.view.displayError(
                    String.format("Input not recognised, please enter a number from 0 to %d.", menu.size())
                );
            }
        }

        return inputNumber - 1;
    }

    private void synchroniseUserFromUserController() {
        this.currentUser = userController.getCurrentUser();
        this.bookingController.setCurrentUser(this.currentUser);
        this.eventPerformanceController.setCurrentUser(this.currentUser);
    }

    public void mainMenu() {

        boolean run = true;

        while (run) {

            int selectionIndex = -1;

            if (this.checkCurrentUserIsStudent()) {

                selectionIndex = this.selectFromMenu(EnumSet.allOf(StudentMenuOptions.class), "");

                if (selectionIndex != -1) {
                    switch (StudentMenuOptions.values()[selectionIndex]) {

                        case LOGOUT:
                            userController.logout();
                            synchroniseUserFromUserController();
                            break;

                        case REVIEW_PERFORMANCE:
                            eventPerformanceController.reviewPerformance();
                            break;

                        case BOOK_EVENT:
                            bookingController.bookEvent();
                            break;

                        case CANCEL_BOOKING:
                            bookingController.cancelBooking();
                            break;

                        default:
                            view.displayError("Feature not implemented yet.");
                    }
                }

            } else if (this.checkCurrentUserIsEntertainmentProvider()) {

                selectionIndex = this.selectFromMenu(EnumSet.allOf(EPMenuOptions.class), "");

                if (selectionIndex != -1) {
                    switch (EPMenuOptions.values()[selectionIndex]) {

                        case LOGOUT:
                            userController.logout();
                            synchroniseUserFromUserController();
                            break;

                        default:
                            view.displayError("Feature not implemented yet.");
                    }
                }

            } else if (this.checkCurrentUserIsAdmin()) {

                selectionIndex = this.selectFromMenu(EnumSet.allOf(AdminMenuOptions.class), "");

                if (selectionIndex != -1) {
                    switch (AdminMenuOptions.values()[selectionIndex]) {

                        case LOGOUT:
                            userController.logout();
                            synchroniseUserFromUserController();
                            break;

                        case SPONSOR_PERFORMANCE:
                            eventPerformanceController.sponsorPerformance();
                            break;

                        default:
                            view.displayError("Feature not implemented yet.");
                    }
                }

            } else if (this.checkCurrentUserIsGuest()) {

                selectionIndex = this.selectFromMenu(EnumSet.allOf(GuestMenuOptions.class), "");

                if (selectionIndex != -1) {
                    switch (GuestMenuOptions.values()[selectionIndex]) {

                        case LOGIN:
                            userController.login();
                            synchroniseUserFromUserController();
                            break;

                        case REGISTER_EP:
                            userController.registerEntertainmentProvider();
                            break;
                    }
                }
            }

            if (selectionIndex == -1) {
                run = false;
            }
        }
    }
}