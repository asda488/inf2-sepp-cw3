package com.acmecorp.events.Services;

public interface VerificationSystem {

    Boolean verifyStudent(String email);

    Boolean verifyEntertainmentProvider(String businessNumber);
}