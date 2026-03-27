package com.acmecorp;

import java.util.Collection;

public interface View {
    public String getInput(String inputPrompt);
    public void displaySuccess(String successMessage);
    public void displayError(String errorMessage);
    public void displayListofPerformances(Collection<String> listOfPerformanceInfo);
    public void displaySpecificPerformance(String performanceInfo);
    public void displayBookingRecord(String bookingRecord);
}

