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

    
    /**
     * Check if the current Controller user is a guest/logged out
     */
    protected boolean checkCurrentUserIsGuest(){
        return currentUser == null; 
    };
    /**
     * Check if the current Controller user is an admin
     */
    protected boolean checkCurrentUserIsAdmin(){
        return currentUser instanceof AdminStaff;
    };
    /**
     * Check if the current Controller user is a student
     */
    protected boolean checkCurrentUserIsStudent(){
        return currentUser instanceof Student;
    };
    /**
     * Check if the current Controller user is an EP
     */
    protected boolean checkCurrentUserIsEntertainmentProvider(){
        return currentUser instanceof EntertainmentProvider;
    };

    /**
     * Select an item from menu 
     */
    public <T> int selectFromMenu(Collection<T> menu, String item){
        throw new UnsupportedOperationException("Not supported yet.");
    };

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
