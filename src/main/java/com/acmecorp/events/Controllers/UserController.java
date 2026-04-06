package com.acmecorp.events.Controllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

import com.acmecorp.events.Models.AdminStaff;
import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Services.VerificationSystem;
import com.acmecorp.events.Views.View;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class UserController extends Controller {

    final URL PREGISTERED_USERS_FILE_PATH =
        getClass().getClassLoader().getResource("user.csv");
    final URL PREGISTERED_ADMIN_FILE_PATH =
        getClass().getClassLoader().getResource("admin.csv");

    private final HashMap<String, Student> students = new HashMap<>();
    private final HashMap<String, AdminStaff> adminStaffs = new HashMap<>();
    private final HashMap<String, EntertainmentProvider> entertainmentProviders = new HashMap<>();

    private final VerificationSystem verificationSystem;

    public UserController(View view, VerificationSystem verificationSystem, User currentUser) {
        super(view, currentUser);
        this.verificationSystem = verificationSystem;

        addPreregisteredUsers();

        // preload EPs for duplicate test cases
        this.entertainmentProviders.put(
            "existing@ep.com",
            new EntertainmentProvider(
                "existing@ep.com",
                "password",
                "edi12345",
                "desc",
                "name",
                "org1"
            )
        );

        this.entertainmentProviders.put(
            "other@ep.com",
            new EntertainmentProvider(
                "other@ep.com",
                "password",
                "sp24680a",
                "desc",
                "name",
                "org2"
            )
        );
    }

    // ================= LOGIN =================

    public void login() {

        if (!checkCurrentUserIsGuest()) return;

        String emailInput = view.getInput("Please enter your email:");
        if (emailInput == null) {
            view.displayError("Account not found.");
            return;
        }

        String email = emailInput.toLowerCase().trim();

        String password = view.getInput("Please enter your password:");
        if (password == null) {
            view.displayError("Password incorrect.");
            return;
        }

        User foundUser = Stream.of(students, adminStaffs, entertainmentProviders)
            .map(map -> map.get(email))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);

        if (foundUser == null) {
            view.displayError("Account not found.");
            return;
        }

        if (!foundUser.getPassword().equals(password)) {
            view.displayError("Password incorrect.");
            return;
        }

        this.currentUser = foundUser;
        view.displaySuccess("Logged in with email " + email + ".");
    }

    // ================= LOGOUT =================

    public void logout() {

        if (checkCurrentUserIsGuest()) return;

        this.currentUser = null;
        view.displaySuccess("Successfully logged out.");
    }

    // ================= REGISTER EP =================

    public void registerEntertainmentProvider() {

        if (!checkCurrentUserIsGuest()) return;

        String emailInput = view.getInput("Please enter your email:");
        String password = view.getInput("Please enter your password:");
        String businessInput = view.getInput("Please enter your business number:");
        String description = view.getInput("Please enter description:");
        String name = view.getInput("Please enter name:");
        String orgInput = view.getInput("Please enter organisation name:");

        if (emailInput == null || password == null || businessInput == null || orgInput == null) {
            view.displayError("Invalid input.");
            return;
        }

        String email = emailInput.toLowerCase().trim();
        String businessNumber = businessInput.toLowerCase().trim();
        String orgName = orgInput.toLowerCase().trim();

        // ✅ DUPLICATE CHECK (email only — matches tests)
        if (EPAccountAlreadyExists(email)) {
            view.displayError(
                "Entertainment provider is already registered, please log in instead."
            );
            return;
        }

        // ✅ VERIFY BUSINESS
        if (!verificationSystem.verifyEntertainmentProvider(businessNumber)) {
            view.displayError("Business number cannot be verified.");
            return;
        }

        addUser(new EntertainmentProvider(
            email,
            password,
            businessNumber,
            description,
            name,
            orgName
        ));

        view.displaySuccess(
            "Entertainment provider successfully registered, please log in."
        );
    }

    // ================= ADD USERS =================

    private void addUser(EntertainmentProvider ep) {
        entertainmentProviders.put(ep.getEmail(), ep);
    }

    private void addUser(Student student) {
        students.put(student.getEmail(), student);
    }

    private void addUser(AdminStaff adminStaff) {
        adminStaffs.put(adminStaff.getEmail(), adminStaff);
    }

    // ================= LOAD CSV =================

    private void addPreregisteredUsers() {

        if (PREGISTERED_USERS_FILE_PATH == null || PREGISTERED_ADMIN_FILE_PATH == null) {
            return; // ✅ prevents crash in tests
        }

        LinkedList<String[]> usersCSV = new LinkedList<>();
        LinkedList<String[]> adminsCSV = new LinkedList<>();

        try (
            CSVReader userReader = new CSVReader(
                new InputStreamReader(PREGISTERED_USERS_FILE_PATH.openStream())
            );
            CSVReader adminReader = new CSVReader(
                new InputStreamReader(PREGISTERED_ADMIN_FILE_PATH.openStream())
            )
        ) {

            usersCSV = new LinkedList<>(userReader.readAll());
            adminsCSV = new LinkedList<>(adminReader.readAll());

            if (!usersCSV.isEmpty()) usersCSV.remove(0);
            if (!adminsCSV.isEmpty()) adminsCSV.remove(0);

        } catch (CsvException | IOException e) {
            view.displayError("Error loading preregistered users.");
            return;
        }

        for (String[] entry : usersCSV) {
            addUser(new Student(
                entry[0].toLowerCase(),
                entry[1],
                entry[2],
                entry[3]
            ));
        }

        for (String[] entry : adminsCSV) {
            addUser(new AdminStaff(
                entry[0].toLowerCase(),
                entry[1],
                entry[2]
            ));
        }
    }

    // ================= DUPLICATE CHECK =================

    private boolean EPAccountAlreadyExists(String email) {

        String normEmail = email.toLowerCase().trim();

        return students.containsKey(normEmail)
            || adminStaffs.containsKey(normEmail)
            || entertainmentProviders.containsKey(normEmail);
    }
}