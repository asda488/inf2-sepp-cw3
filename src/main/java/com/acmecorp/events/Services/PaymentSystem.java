package com.acmecorp.events.Services;

public interface PaymentSystem {
    public Boolean processPayment(int numTickets, String eventTitle, String studentEmail,
        String studentPhone, String epEmail, double transactionAmount);
    public Boolean processRefund(int numTickets, String eventTitle, String studentEmail,
        String studentPhone, String epEmail, double transactionAmount, String organiserMsg);
}
