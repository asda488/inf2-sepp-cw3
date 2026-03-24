package com.acmecorp;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import com.opencsv.CSVReader;

public class UserController extends Controller{
    final String PREGISTERED_USERS_FILE_PATH = "src/main/resources/users.csv";
    final String PREGISTERED_ADMIN_FILE_PATH = "src/main/resources/admin.csv";

    private User currentUser;

    //Email indexed hashmaps to store registered users
    private HashMap<String, Student> students;
    private HashMap<String, AdminStaff> adminStaffs;
    private HashMap<String, EntertainmentProvider> entertainmentProviders;

    //constructor
    public UserController() throws Exception {
        this.currentUser = null;
        this.addPreregisteredUsers();
    }

    /**
     * Check if the current UserController user is a guest
     */
    @Override
    protected Boolean checkCurrentUserIsGuest(){
        return currentUser == null; 
    }

    /**
     * Check if the current UserController user is an admin
     */
    @Override
    protected Boolean checkCurrentUserIsAdmin(){
        return currentUser instanceof AdminStaff;
    }


    /**
     * Check if the current UserController user is a student
     */
    @Override
    protected Boolean checkCurrentUserIsStudent(){
        return currentUser instanceof Student;
    }

    /**
     * Check if the current UserController user is an EP
     */
    @Override
    protected Boolean checkCurrentUserIsEntertainmentProvider(){
        return currentUser instanceof EntertainmentProvider;
    }

    /**
     * Login a user using their email and plaintext password, updating the currentUser if login is found and successful.
     * Returns whether login was successful or not.
     */
    public Boolean login(String email, String password){
        this.currentUser = Stream.of(this.students, this.adminStaffs, this.entertainmentProviders)
            .map(user -> user.get(email))
            .findFirst()
            .orElse(null);
        return this.currentUser != null;
    }

    /**
     * Logs the user out, setting the currentUser to null
     */
    public void logout(){
        this.currentUser = null;
    }

    /**
     * Attempt to register an entertainment provider, returning true or false depending on success
     */
    public Boolean registerEntertainmentProvider(String email, String password, 
        String businessNumber, String description, String name, String orgName){
        if (this.EPAccountAlreadyExists(email, orgName, businessNumber)){
            return false;
        }
        addUser(new EntertainmentProvider(email, password, businessNumber, description, name, orgName));
        return true;
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
    private void addPreregisteredUsers() throws Exception {
        //read csvs into lists
        List<String[]> usersCSV;
        List<String[]> adminsCSV;
        usersCSV = new CSVReader(new FileReader(PREGISTERED_USERS_FILE_PATH)).readAll();
        adminsCSV = new CSVReader(new FileReader(PREGISTERED_ADMIN_FILE_PATH)).readAll();

        //remove headers
        usersCSV.remove(0);
        adminsCSV.remove(0);

        //create objects from CSV lists and add to internal lists
        for (String[] entry : usersCSV) {
            addUser(new Student(entry[0], entry[1], entry[2], Integer.parseInt(entry[3])));
        }
        for (String[] entry : adminsCSV) {
            addUser(new AdminStaff(entry[0], entry[1], entry[2]));
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
        ;
    }
}
