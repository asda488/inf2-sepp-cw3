package com.acmecorp.events.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.Event;
import com.acmecorp.events.Models.Performance;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Views.View;

public class EventPerformanceController extends Controller{
    private long nextEventID;
    private long nextPerformanceID;
    private List<Event> events;

    public EventPerformanceController(View view, User currentUser) {
        super(view, currentUser);
        this.nextEventID = 0;
        this.nextPerformanceID = 0;
        this.events = new ArrayList<>();
    }

    public Event createEvent() {
        String title;
        Event.EventType eventType;
        Boolean isTicketed;
        int noOfPerformances;

        while (true) {
            title = this.view.getInput("What is the name of the event?").trim();
            if (!title.isEmpty()) {
                break; //valid input causes loop exit
            }
            this.view.displayError("Invalid input, try again.");
            }

        while (true) {
            String input = this.view.getInput("What is the event type? (Music/Theatre/Dance/Movie/Sports)").trim();
            try {
                eventType = Event.EventType.valueOf(input.toUpperCase());
                break; // valid input causes loop exit
            } catch (IllegalArgumentException e) {
                this.view.displayError("Invalid type, try again.");
            }
        }

        while (true) {
            String ticketedInput = this.view.getInput("Is the event ticketed (yes/no):").trim();
            isTicketed = ticketedInput.equalsIgnoreCase("yes");
            if (ticketedInput.equalsIgnoreCase("yes") || ticketedInput.equalsIgnoreCase("no")) {
                break; // valid input causes loop exit
            }
            this.view.displayError("Invalid choice, try again.");
        }

        while (true) {
            try {
                noOfPerformances = Integer.parseInt(this.view.getInput("Enter number of performances:"));
                break; // valid input causes loop exit
            } catch (NumberFormatException e) {
                this.view.displayError("Invalid number, try again.");
            }
        }

        Event event = new Event(nextEventID, title, eventType, isTicketed, new ArrayList<>(), 
            (EntertainmentProvider)this.currentUser);
        events.add(event);
        nextEventID++;

        for (int i = 1; i <= noOfPerformances; i++) {

            LocalDateTime startDateTime;
            LocalDateTime endDateTime;
            String performersInput;
            Double ticketPrice;

            while (true) {
                try {
                    startDateTime = LocalDateTime.parse(this.view.getInput(
                        "Performance " + i + "\n" +
                        "Enter performance start date and time (yyyy-MM-dd HH:mm):"));
                    break; // valid input causes loop exit
                } catch (NumberFormatException e) {
                    this.view.displayError("Invalid input, try again.");
                }
            }

            while (true) {
                try {
                    endDateTime = LocalDateTime.parse(this.view.getInput(
                        "Enter performance end date and time (yyyy-MM-dd HH:mm):"));
                    break; // valid input causes loop exit
                } catch (NumberFormatException e) {
                    this.view.displayError("Invalid input, try again.");
                }
            }

            while (true) {
                performersInput = this.view.getInput(
                    "Enter name(s) of the performer(s) (comma-separated):").trim();
                if (!performersInput.isEmpty()) {
                    break; // valid input causes loop exit
                }
                this.view.displayError("Invalid string, try again.");
            }
            List<String> performerNames = Arrays
                .stream(performersInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList()); 


            while (true) {
                try {
                    ticketPrice = Double.parseDouble(this.view.getInput("Enter the ticket price: £"));
                    break; // valid input causes loop exit
                } catch (NumberFormatException e) {
                    this.view.displayError("Invalid price, try again.");
                }
            }

            int numTickets;
            String venueAddress;
            int venueCapacity;

            if (isTicketed) {
                while (true) {
                    try {
                        numTickets = Integer.parseInt(this.view.getInput("Enter the remaining number of tickets:"));
                        if (numTickets <= 0) {
                            throw new Exception(); //throw exception on invalid number
                        }
                        break;  
                    } catch (Exception e) {
                        this.view.displayError("Invalid number, try again.");
                    }
                }
            } else {
                numTickets = 0;
            }

            while (true) {
                venueAddress = this.view.getInput("Enter the venue address:").trim();
                if (!venueAddress.isEmpty()) {
                    break; //valid input causes loop exit
                }
                this.view.displayError("Invalid input, try again.");
            }

            while (true) {
                try {
                    venueCapacity = Integer.parseInt(this.view.getInput("Enter the venue capacity:"));
                    if (venueCapacity <= 0) {
                        throw new Exception(); //throw exception on invalid number
                    }
                    break;
                } catch (Exception e) {
                    this.view.displayError("Invalid number, try again.");
                }
            }


            boolean venueIsOutdoors;
            boolean venueAllowsSmoking;

            while (true) {
                String outdoorsInput = this.view.getInput("Is the venue outdoors (yes/no):").trim();
                venueIsOutdoors = outdoorsInput.equalsIgnoreCase("yes");
                if (outdoorsInput.equalsIgnoreCase("yes") || outdoorsInput.equalsIgnoreCase("no")) {
                    break; // valid input causes loop exit
                }
                this.view.displayError("Invalid choice, try again.");
            }

            while (true) {
                String smokingInput = this.view.getInput("Does the venue allow smoking (yes/no):").trim();
                venueAllowsSmoking = smokingInput.equalsIgnoreCase("yes");
                if (smokingInput.equalsIgnoreCase("yes") || smokingInput.equalsIgnoreCase("no")) {
                    break; // valid input causes loop exit
                }
                this.view.displayError("Invalid choice, try again.");
            }

            event.createPerformance(nextPerformanceID, startDateTime, endDateTime, performerNames, 
                ticketPrice, numTickets, venueAddress, venueCapacity, 
                venueIsOutdoors, venueAllowsSmoking);
            nextPerformanceID++;
        }
        return event;
    }

    public void searchForPerformances() {
        LocalDateTime searchDateTime;
        while (true) {
            while (true) { 
                try {
                    searchDateTime = LocalDateTime.parse(this.view.getInput("Enter search date for performances (yyyy-MM-dd): "));
                    break;
                } catch (Exception e) {
                    this.view.displayError("Invalid date, try again.");
                }
            }
            if (!searchDateTime.isBefore(LocalDateTime.now())) {
                break; // valid input causes loop exit
            }
            this.view.displayError("Invalid date, try again.");
        }

        Collection<String> performances = new ArrayList<>();
        for (Event e : events) {
            performances = e.getInfoForPerformancesOnDate(searchDateTime);
            for (String p : performances) {
                performances.add(p);
            }
        }

        if (performances.isEmpty()) {
            this.view.displayError("There are no performances on " + searchDateTime.toLocalDate().toString());
            return;
        }

        if (this.checkCurrentUserIsStudent()) {
            //currentUser.getPreferences();
        }

        this.view.displayListofPerformances(performances);
        this.view.displaySuccess("Performances on " + searchDateTime.toLocalDate().toString() + "successfully found");
    }

    public void viewPerformance() {
        long selectedPerformanceID;
        while (true) {
            try {
                selectedPerformanceID = Long.parseLong(this.view.getInput("Enter PerformanceID: "));
                break;
            } catch (NumberFormatException e) {
                this.view.displayError("Invalid ID, try again");
            }
        }

        Performance performance = getPerformanceByID(selectedPerformanceID);
        if (performance == null) {
            this.view.displayError("No performance exists with ID " + String.valueOf(selectedPerformanceID));
        } else {
            this.view.displaySpecificPerformance("Event = " + performance.getEvent().getTitle() + 
            ", PerformanceID = " + String.valueOf(performance.getPerformanceID()) + 
            ", Start Date and Time = " + performance.getStartDateTime().toString() + 
            ", End Date and Time = " + performance.getEndDateTime().toString() + 
            ", Performers = " + performance.getPerformerNames() + 
            ", Price = " + String.valueOf(performance.getTicketPrice()) + 
            ", No. of Tickets = " + String.valueOf(performance.getNumTicketsTotal() - performance.getNumTicketsSold()) + 
            ", Venue Address = " + performance.getVenueAddress() + 
            ", Capacity = " + String.valueOf(performance.getVenueCapacity()) + 
            ", Outdoors = " + String.valueOf(performance.getVenueIsOutdoors()) + 
            ", Smoking = " + String.valueOf(performance.getVenueAllowsSmoking()) + 
            ", Status = " + String.valueOf(performance.getStatus()));
            this.view.displaySuccess("Performance with ID " + String.valueOf(selectedPerformanceID) + " successfully found");
        }
    }

    private Event getEventByID(long eventID) {
        for (Event e : events) {
            if (eventID == e.getEventID()) {
                return e;
            }
        }
        return null;
    }

    private Event getEventByTitle(String title) {
        for (Event e : events) {
            if (title.equals(e.getTitle())) {
                return e;
            }
        }
        return null;
    }

    private Performance getPerformanceByID(long performanceID) {
        for (Event e : events) {
            List<Performance> performances = e.getPerformances();
            for (Performance p : performances) {
                if (performanceID == p.getPerformanceID()) {
                    return p;
                }
            }
        }
        return null;
    }
}