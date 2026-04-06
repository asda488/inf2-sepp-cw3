package com.acmecorp.events.Services;

public interface PaymentSystem {

    Boolean processPayment(int numTickets, String eventTitle, String studentEmail,
                           String studentPhone, String epEmail, double transactionAmount);

    Boolean processRefund(int numTickets, String eventTitle, String studentEmail,
                          String studentPhone, String epEmail, double transactionAmount,
                          String organiserMsg);
}