package com.acmecorp.events;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.FieldSource;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegisterEPSystemTests extends SystemTestsBase{
    static List<Arguments> VALID_EP_CREDENTIALS = Arrays.asList(
        arguments("admin@edinburghtheatre.com", "editheatre", "edi12345", 
            "Edinburgh Theatres and Performing Arts", "Liam Theatremaster", "Edinburgh Theatres"),
        arguments("circus@londoncircus.com", "f!nest", "lon98765",
            "London's finest touring circus", "Mr Clown", "London Circus"),
        arguments("sales@comart.org.uk", "comart57", "sp24680a",
            "Official Community Art Centre", "Polly K", "Community Art Centre")
    );

    static List<Arguments> INVALID_EXISTING_EP_CREDENTIALS = Arrays.asList(
        //same existing email
        arguments("admin@edinburghtheatre.com", "Edinburgh Theatres", "edi12345",
        "admin@edinburghtheatre.com", "Hindenburgh Theatres", "edi12346", "email"),
        arguments("circus@londoncircus.com", "London Circus", "lon98765",
        "circus@londoncircus.com", "Manchester Circus", "lon98766", "email"),
        arguments("sales@comart.org.uk", "Community Art Centre", "sp24680a",
            "sales@comart.org.uk", "Unofficial Community Art Centre", "sp24680b", "email"),

        //same existing name
        arguments("admin@edinburghtheatre.com", "Edinburgh Theatres", "edi12345",
            "admin@edinburghtheatre.co.uk", "Edinburgh Theatres", "edi12346", "organisation name"),
        arguments("circus@londoncircus.com", "London Circus", "lon98765",
            "sales@londoncircus.com", "London Circus", "lon98766", "organisation name"),
        arguments("sales@comart.org.uk", "Community Art Centre", "sp24680a",
            "comart@ukcoms.org", "Community Art Centre", "sp24680b", "organisation name"),

        //same existing business number
        arguments("admin@edinburghtheatre.com", "Edinburgh Theatres", "edi12345",
            "admin@edinburghtheatre.co.uk", "Hindenburgh Theatres", "edi12345", "business number"),
        arguments("circus@londoncircus.com", "London Circus", "lon98765",
            "circus@londoncircus.com", "Manchester Circus", "lon98765", "business number"),
        arguments("sales@comart.org.uk", "Community Art Centre", "sp24680a",
            "sales@comart.org.uk", "Unofficial Community Art Centre", "sp24680a", "business number")
    );

    static List<Arguments> INVALID_WRONG_BUSINESS_NUMBER_EP_CREDENTIALS = Arrays.asList(
        arguments("admin@edinburghtheatre.com", "Edinburgh Theatres", "73unwe723"),
        arguments("circus@londoncircus.com", "London Circus", "wrongnumber"),
        arguments("sales@comart.org.uk", "Official Community Art Centre", "sp24680b")
    );

    void attemptRegisterEPAndExit(String email, String password, String businessNumber, 
        String description, String name, String orgName){
        when(this.textUserInterfaceMock.getInput(Mockito.contains("email"))).thenReturn(email); //fill in email
        when(this.textUserInterfaceMock.getInput(Mockito.contains("password"))).thenReturn(password); //fill in password
        when(this.textUserInterfaceMock.getInput(Mockito.contains("business number")))
            .thenReturn(businessNumber); //fill in business number
        when(this.textUserInterfaceMock.getInput(Mockito.contains("description"))).thenReturn(description); //fill in description
        when(this.textUserInterfaceMock.getInput(Mockito.contains("name"))).thenReturn(name); //fill in name
        when(this.textUserInterfaceMock.getInput(Mockito.contains("organisation name")))
            .thenReturn(orgName); //fill in org name, overrides "name" above when needed

        //while on a menu, select the register EP action, and then exit the next time
        when(this.textUserInterfaceMock.getInput(Mockito.contains("Menu"))).thenReturn("2").thenReturn("0"); 

        this.menuController.mainMenu();
    }
    
    /** Check that EPs can be registered with the correct credentials */
    @ParameterizedTest
    @FieldSource("VALID_EP_CREDENTIALS")
    void registerEPSuccess(String email, String password, String businessNumber, 
        String description, String name, String orgName){
        attemptRegisterEPAndExit(email, password, businessNumber, description, name, orgName);
        verify(this.textUserInterfaceMock, times(1)
            .description("EP registration was unsuccessful even though correct credentials were provided."))
            .displaySuccess("Entertainment provider successfully registered, please log in.");
    }

    /** Check that invalid business numbers will not be accepted. */
    @ParameterizedTest
    @FieldSource("INVALID_WRONG_BUSINESS_NUMBER_EP_CREDENTIALS")
    void registerInvalidBusinessNumberEPFail(String email, String orgName, String businessNumber){
        attemptRegisterEPAndExit(email, "", businessNumber, "", "", orgName);
        verify(this.textUserInterfaceMock, times(1)
            .description("EP registration was successful even though wrong business number was provided."))
            .displayError("Business number cannot be verified.");
    }
    
    /** Check that duplicate registrations (same email, organisation name, business number) fail. */
    @ParameterizedTest
    @FieldSource("INVALID_EXISTING_EP_CREDENTIALS")
    void registerExistingEPFail(String email, String orgName, String businessNumber,
        String duplicateEmail, String duplicateOrgName, String duplicateBusinessNumber, String duplicateType){
        attemptRegisterEPAndExit(email, "", businessNumber, "", "", orgName);
        attemptRegisterEPAndExit(duplicateEmail, "", duplicateBusinessNumber, "", "", duplicateOrgName);
        verify(this.textUserInterfaceMock, times(1)
            .description(String.format("EP registration was successful even though duplicate %s was provided.", duplicateType)))
            .displayError("Entertainment provider is already registered, please log in instead.");
    }
}