package com.acmecorp.events.Services;

public class MockVerificationSystem implements VerificationSystem {

    @Override
    public Boolean verifyStudent(String email) {
        return email != null && email.contains("@");
    }

    @Override
    public Boolean verifyEntertainmentProvider(String businessNumber) {
        if (businessNumber == null) return false;

        String bn = businessNumber.trim().toLowerCase();

        return !bn.isEmpty() && bn.length() >= 5;
    }
}