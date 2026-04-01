package com.acmecorp.events.Views;

import java.util.Collection;
import java.util.Scanner;

/**
 * Class to handle all text input and outputs, with specified methods for specific output messages.
 */
public class TextUserInterface implements View {
    private final Scanner scanner;

    /**
     * Constructor makes single scanner object for entire program lifetime
     */
    public TextUserInterface() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays an input prompt and reads one line as a String from the input (console)
     * @param inputPrompt String to prompt the user with for input
     */
    @Override
    public String getInput(String inputPrompt){
        System.out.print(String.format("%s: ", inputPrompt));
        return this.scanner.nextLine();
    }

    /**
     * Display a success message to the user
     * @param successMessage Success message as a String
     */
    @Override
    public void displaySuccess(String successMessage){
        System.out.println(String.format("[SUCCESS] %s", successMessage));
    }

    /**
     * Display an error message to the user
     * @param errorMessage Error message as a String
     */
    @Override
    public void displayError(String errorMessage){
        System.out.println(String.format("[ERROR] %s", errorMessage));
    }

    @Override
    public void displayListofPerformances(Collection<String> listOfPerformanceInfo){

    }

    @Override
    public void displaySpecificPerformance(String performanceInfo){

    }
    
    @Override
    public void displayBookingRecord(String bookingRecord){
        
    }
}
