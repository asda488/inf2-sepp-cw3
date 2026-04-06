package com.acmecorp.events.Models;

public class EntertainmentProvider extends User {
    private String orgName;
    private String businessNumber;
    private String name;
    private String description;

    public EntertainmentProvider(String email, String password, 
        String businessNumber, String description, String name, String orgName) {
        super(email, password);
        this.businessNumber = businessNumber;
        this.description = description;
        this.name = name;
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgEmail() {
        return this.getEmail();
    }
}
