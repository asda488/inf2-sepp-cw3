package com.acmecorp.events.Views;

import java.util.Collection;
import java.util.Scanner;

/**
 * Class to handle all text input and outputs, with specified methods for specific output messages.
 */
public class TextUserInterface implements View {

    private final Scanner scanner;

    public TextUserInterface() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getInput(String inputPrompt) {
        System.out.print(String.format("%s: ", inputPrompt));
        return this.scanner.nextLine();
    }

    @Override
    public void displaySuccess(String successMessage) {
        System.out.println(String.format("[SUCCESS] %s", successMessage));
    }

    @Override
    public void displayError(String errorMessage) {
        System.out.println(String.format("[ERROR] %s", errorMessage));
    }

    @Override
    public void displayListofPerformances(Collection<String> listOfPerformanceInfo) {
        if (listOfPerformanceInfo == null || listOfPerformanceInfo.isEmpty()) {
            System.out.println("No performances available.");
            return;
        }

        StringBuilder output = new StringBuilder("Performances:\n");
        int i = 1;

        for (String performance : listOfPerformanceInfo) {
            output.append(i++)
                  .append(". ")
                  .append(performance)
                  .append("\n");
        }

        System.out.print(output);
    }

    @Override
    public void displaySpecificPerformance(String performanceInfo) {
        if (performanceInfo == null || performanceInfo.trim().isEmpty()) {
            System.out.println("No performance details available.");
            return;
        }

        System.out.println("Performance Details:");
        System.out.println(performanceInfo.trim());
    }

    @Override
    public void displayBookingRecord(String bookingRecord) {
        if (bookingRecord == null || bookingRecord.trim().isEmpty()) {
            System.out.println("No booking record available.");
            return;
        }

        System.out.println("Booking Record:");
        System.out.println(bookingRecord.trim());
    }
}