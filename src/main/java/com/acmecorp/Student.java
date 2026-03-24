package com.acmecorp;

public class Student extends User{
    private String name;
    private int phoneNumber;

    /**
     * Constructor for student
     * @param email Email of student
     * @param password Plaintext password of student
     * @param name Full name of student
     * @param phoneNumber Phone number of student as an integer
     */
    public Student(String email, String password, String name, int phoneNumber) {
        super(email, password);
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    
}
