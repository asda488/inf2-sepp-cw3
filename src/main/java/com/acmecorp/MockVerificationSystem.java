package com.acmecorp;

import java.util.Arrays;
import java.util.HashSet;

public class MockVerificationSystem implements VerificationSystem {

    HashSet<String> businessNumbers = new HashSet<>(Arrays.asList(
        "edi12345",
        "lon98765",
        "sp24680a"
    ));

    @Override
    public Boolean verifyEntertainmentProvider(String businessRegistrationNumber) {
        return businessNumbers.contains(businessRegistrationNumber.toLowerCase());
    }
    
}
