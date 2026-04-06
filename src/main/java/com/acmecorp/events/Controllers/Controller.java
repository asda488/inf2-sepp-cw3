package com.acmecorp.events.Controllers;

import java.util.Collection;

import com.acmecorp.events.Models.AdminStaff;
import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Views.View;

public abstract class Controller {

    protected View view;
    protected User currentUser;

    public Controller(View view, User currentUser) {
        this.view = view;
        this.currentUser = currentUser;
    }

    protected boolean checkCurrentUserIsGuest() {
        return currentUser == null;
    }

    protected boolean checkCurrentUserIsAdmin() {
        return currentUser instanceof AdminStaff;
    }

    protected boolean checkCurrentUserIsStudent() {
        return currentUser instanceof Student;
    }

    protected boolean checkCurrentUserIsEntertainmentProvider() {
        return currentUser instanceof EntertainmentProvider;
    }

    public <T> int selectFromMenu(Collection<T> menu, String item) {
        int inputNumber = -1;

        while (!(inputNumber >= 0 && inputNumber <= menu.size())) {

            StringBuilder fullMenu = new StringBuilder("Menu:\n");
            int i = 1;

            for (T t : menu) {
                fullMenu.append(i)
                        .append(". ")
                        .append(t.toString())
                        .append("\n");
                i++;
            }

            String input = view.getInput(
                fullMenu + "Enter a number (0 to exit):"
            );

            if (input == null) {
                view.displayError("Invalid input.");
                continue;
            }

            try {
                inputNumber = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                view.displayError("Invalid input.");
            }
        }

        return inputNumber - 1;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}