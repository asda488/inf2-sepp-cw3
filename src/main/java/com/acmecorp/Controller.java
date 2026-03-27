package com.acmecorp;

import java.util.Collection;

public abstract class Controller {

    protected TextUserInterface textUserInterface;
    protected User currentUser;

    public Controller(TextUserInterface textUserInterface, User currentUser) {
        this.textUserInterface = textUserInterface;
        this.currentUser = currentUser;
    }

    
    /**
     * Check if the current Controller user is a guest/logged out
     */
    protected Boolean checkCurrentUserIsGuest(){
        return currentUser == null; 
    };
    /**
     * Check if the current Controller user is an admin
     */
    protected Boolean checkCurrentUserIsAdmin(){
        return currentUser instanceof AdminStaff;
    };
    /**
     * Check if the current Controller user is a student
     */
    protected Boolean checkCurrentUserIsStudent(){
        return currentUser instanceof Student;
    };
    /**
     * Check if the current Controller user is an EP
     */
    protected Boolean checkCurrentUserIsEntertainmentProvider(){
        return currentUser instanceof EntertainmentProvider;
    };

    /**
     * Select an item from menu 
     */
    public <T> int selectFromMenu(Collection<T> menu, String item){
        throw new UnsupportedOperationException("Not supported yet.");
    };
}
