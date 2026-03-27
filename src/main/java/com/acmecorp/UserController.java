package com.acmecorp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Stream;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class UserController extends Controller{
    final URL PREGISTERED_USERS_FILE_PATH = getClass().getClassLoader().getResource("user.csv");
    final URL PREGISTERED_ADMIN_FILE_PATH = getClass().getClassLoader().getResource("admin.csv");

    //Email indexed hashmaps to store registered users
    private HashMap<String, Student> students = new HashMap<>();
    private HashMap<String, AdminStaff> adminStaffs = new HashMap<>();
    private HashMap<String, EntertainmentProvider> entertainmentProviders = new HashMap<>();

    //constructor
    public UserController(TextUserInterface textUserInterface, User currentUser){
        super(textUserInterface, currentUser);
        this.addPreregisteredUsers();
    }

    /**
     * Login a user using their email and plaintext password, 
     * updating the currentUser if login is found and successful.
     */
    public void login(){
        //get user's credentials and look up the user by email
        String email = this.textUserInterface.getInput("Please enter your email").toLowerCase();
        String password = this.textUserInterface.getInput("Please enter your password");
        User foundUser = Stream.of(this.students, this.adminStaffs, this.entertainmentProviders)
            .map(user -> user.get(email))
            .findFirst()
            .orElse(null);
        
        //login if found and password is correct, else error
        if (foundUser != null){
            if (foundUser.getPassword().equals(password)){
                this.currentUser = foundUser;
                this.textUserInterface.displaySuccess(String.format("Logged in with email %s, account type %s.", email, currentUser.getClass()));

            } else {
                this.textUserInterface.displayError("Password incorrect.");
            }

        } else {
            this.textUserInterface.displayError("Account not found.");
        }
    }

    /**
     * Logs the user out, setting the currentUser to null
     */
    public void logout(){
        this.currentUser = null;
        textUserInterface.displaySuccess("Successfully logged out.");
    }

    /**
     * Attempt to register an entertainment provider using the correct details, failing if it is already registered
     */
    public void registerEntertainmentProvider(){
        //ask for initial information
        String email = this.textUserInterface.getInput("Please enter the email for the entertainment provider account").toLowerCase();
        String orgName = this.textUserInterface.getInput("Please enter the organsation name of the entertainment provider");
        String businessNumber = this.textUserInterface.getInput("Please enter the business number of the entertainment provider").toLowerCase();

        //TODO: mock verification system full mock
        //TODO: input validation

        //if initial information finds an EP, fail, else carry on with registration
        if (this.EPAccountAlreadyExists(email, orgName, businessNumber)){
            this.textUserInterface.displayError("Entertainment provider is already registered, please log in instead.");
        } else {
            String password = this.textUserInterface.getInput("Please enter the password for the entertainment provider account");
            String description = this.textUserInterface.getInput("Please enter a description for the entertainment provider account");
            String name = this.textUserInterface.getInput("Please enter a name for the entertainment provider account");
            addUser(new EntertainmentProvider(email, password, businessNumber, description, name, orgName));
            this.textUserInterface.displaySuccess("Entertainment provider successfully registered, please log in.");
        }
    }

    public void editPreferences(){

    }

    /**
     * Adds one entertainment provider to internal tracked hashmap of registered providers
     * @param entertainmentProvider EP to be added
     */
    private void addUser(EntertainmentProvider entertainmentProvider){
        this.entertainmentProviders.put(entertainmentProvider.getEmail(), entertainmentProvider);
    }
    /**
     * Adds one student to internal tracked hashmap of registered students
     * @param entertainmentProvider Student to be added
     */
    private void addUser(Student student){
        this.students.put(student.getEmail(), student);
    }
    /**
     * Adds one admin to internal tracked hashmap of registered admins
     * @param entertainmentProvider Admin to be added
     */
    private void addUser(AdminStaff adminStaff){
        this.adminStaffs.put(adminStaff.getEmail(), adminStaff);
    }

    /**
     * Registers all preregistered users (students, admins) in the given files.
     * Called during start-up/constructor
     */
    private void addPreregisteredUsers() {
        //read csvs into lists
        LinkedList<String[]> usersCSV = new LinkedList<>();
        LinkedList<String[]> adminsCSV = new LinkedList<>();
        try {
            usersCSV = (LinkedList)(new CSVReader(new InputStreamReader(PREGISTERED_USERS_FILE_PATH.openStream())).readAll());
            adminsCSV = (LinkedList)(new CSVReader(new InputStreamReader(PREGISTERED_ADMIN_FILE_PATH.openStream())).readAll());

            //remove headers
            usersCSV.remove(0);
            adminsCSV.remove(0);
        } catch (CsvException | IOException e){
            this.textUserInterface.displayError("Error in pre-registering users, list of pre-registered users and admins will be empty.");
        }

        //create objects from CSV lists and add to internal lists
        for (String[] entry : usersCSV) {
            addUser(new Student(entry[0].toLowerCase(), entry[1], entry[2], entry[3]));
        }
        for (String[] entry : adminsCSV) {
            addUser(new AdminStaff(entry[0].toLowerCase(), entry[1], entry[2]));
        }
    }

    private Boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber){
        //firstly check if there is an EP account with the given email
        if (this.entertainmentProviders.containsKey("email")){
            return true;
        }
        //else manual iteration through the list
        for (EntertainmentProvider ep : this.entertainmentProviders.values()){
            if (ep.getOrgName().equals(orgName) || ep.getBusinessNumber().equals(businessNumber)){
                return true;
            }
        }
        //if none found at all, return false
        return false;
    }

    private EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber){
        //TODO implement
        return new EntertainmentProvider("a", "a", "a", "a", "a", "a");
    }
}
