package com.acmecorp.events.Controllers;

import java.time.LocalDateTime;

import com.acmecorp.events.Models.User;
import com.acmecorp.events.Views.View;

public class EventPerformanceController extends Controller{
    private long nextEventID;
    private long nextPerformanceID;
    private List<Event> events;

    enum EventType {
        MUSIC,
        THEATRE,
        DANCE,
        MOVIE,
        SPORTS
    }

    public EventPerformanceController(View view, User currentUser) {
        super(view, currentUser);
        this.nextEventID = 0;
        this.nextPerformanceID = 0;
        this.events = new ArrayList<>();
    }

    public Event createEvent() {
        while (true) {
                String title = this.view.getInput("What is the name of the event?").trim();
                if (!title.isEmpty()) {
                    break; //valid input causes loop exit
                }
                this.view.displayError("Invalid input, try again.")
            }

        while (true) {
            String type = this.view.getInput("What is the event type? (Music/Theatre/Dance/Movie/Sports)").trim();
            try {
                EventType event = EventType.valueOf(input.toUpperCase());
                break; // valid input causes loop exit
            } catch (IllegalArgumentException e) {
                this.view.displayError("Invalid type, try again.");
            }
        }

        while (true) {
            String ticketedInput = this.view.getInput("Is the event ticketed (yes/no):").trim();
            boolean isTicketed = ticketedInput.equalsIgnoreCase("yes");
            if ticketedInput.equalsIgnoreCase("yes") || ticketedInput.equalsIgnoreCase("no") {
                break; // valid input causes loop exit
            }
            this.view.displayError("Invalid choice, try again.");
        }

        while (true) {
            try {
                int noOfPerformances = Integer.parseInt(this.view.getInput("Enter number of performances:"));
                break; // valid input causes loop exit
            } catch (NumberFormatException e) {
                this.view.displayError("Invalid number, try again.");
            }
        }

        Event event = new Event(nextEventID, title, type, isTicketed, currentUser);
        events.add(event);
        nextEventID++;

        for (int i = 1; i <= noOfPerformances; i++) {
            System.out.println("Performance " + i)

            while (true) {
                try {
                    LocalDateTime startDateTime = LocalDateTime.parse(this.view.getInput("Enter performance start date and time (yyyy-MM-dd HH:mm):"));
                    break; // valid input causes loop exit
                } catch (NumberFormatException e) {
                    this.view.displayError("Invalid input, try again.");
                }
            }

            while (true) {
                try {
                    LocalDateTime endDateTime = LocalDateTime.parse(this.view.getInput("Enter performance end date and time (yyyy-MM-dd HH:mm):"));
                    break; // valid input causes loop exit
                } catch (NumberFormatException e) {
                    this.view.displayError("Invalid input, try again.");
                }
            }

            while (true) {
                String performersInput = this.view.getInput("Enter name(s) of the performer(s) (comma-separated):").trim();
                if (!performersInput.isEmpty()) {
                    break; // valid input causes loop exit
                }
                this.view.displayError("Invalid string, try again.");
            }
            List<String> performerNames = Arrays.stream(performersInput.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList()); 


            while (true) {
                try {
                    double ticketPrice = Double.parseDouble(this.view.getInput("Enter the ticket price: £"));
                    break; // valid input causes loop exit
                } catch (NumberFormatException e) {
                    this.view.displayError("Invalid price, try again.");
                }
            }

            if (isTicketed) {
                while (true) {
                    int numTickets = this.view.getInput("Enter the remaining number of tickets:")
                    if (numTickets > 0) {
                        break; //valid input causes loop exit
                    }
                    this.view.displayError("Invalid number, try again.");
                }
            } else {
                int numTickets = null;
            }

            while (true) {
                String venueAddress = this.view.getInput("Enter the venue address:").trim();
                if (!venueAddress.isEmpty()) {
                    break; //valid input causes loop exit
                }
                this.view.displayError("Invalid input, try again.")
            }

            while (true) {
                int venueCapacity = this.view.getInput("Enter the venue capacity:");
                if (venueCapacity > 0) {
                    break; //valid input causes loop exit
                }
                this.view.displayError("Invalid number, try again.");
            }

            while (true) {
                String outdoorsInput = this.view.getInput("Is the venue outdoors (yes/no):").trim();
                boolean venueIsOutdoors = outdoorsInput.equalsIgnoreCase("yes");
                if outdoorsInput.equalsIgnoreCase("yes") || outdoorsInput.equalsIgnoreCase("no") {
                    break; // valid input causes loop exit
                }
                this.view.displayError("Invalid choice, try again.");
            }

            while (true) {
                String smokingInput = this.view.getInput("Does the venue allow smoking (yes/no):").trim();
                boolean venueAllowsSmoking = smokingInput.equalsIgnoreCase("yes");
                if smokingInput.equalsIgnoreCase("yes") || smokingInput.equalsIgnoreCase("no") {
                    break; // valid input causes loop exit
                }
                this.view.displayError("Invalid choice, try again.");
            }

            event.createPerformance(event, nextPerformanceID, startDateTime, endDateTime, performerNames, ticketPrice, numTickets, venueAddress, venueCapacity, venueIsOutdoors, venueAllowsSmoking);
            nextPerformanceID++;
        }
        return event;
    }

    public void searchForPerformances() {
        while (true) {
            try {
                LocalDate searchDate = LocalDate.parse(this.view.getInput("Enter search date for performances (yyyy-MM-dd): "));
            } catch (DateException e) {
                this.view.displayError("Invalid date, try again.");
            }
            if (!searchDateTime.isBefore(LocalDateTime.now())) {
                break; // valid input causes loop exit
            }
            this.view.displayError("Invalid date, try again.");
        }

        List<Performance> performancesOnDate = new ArrayList<>();
        for (Event e : events) {
            Collection<String> performances = e.getInfoForPerformancesOnDate();
            for (String p : performances) {
                performancesOnDate.add(p);
            }
        }

        if (performancesOnDate.isEmpty()) {
            this.view.displayError("There are no performances on " + searchDateTime.toLocalDate().toString());
            return;
        }

        if (currentUser.checkCurrentUserIsStudent()) {
            //currentUser.getPreferences();
        }

        this.view.displayListofPerformances(performancesOnDate);
        this.view.displaySuccess("Performances on " + searchDateTime.toLocalDate().toString() + "successfully found")
    }

    public void viewPerformance() {
        while (true) {
            try {
                long selectedPerformanceID = Long.parseLong(this.view.getInput("Enter PerformanceID: "))
            } catch (NumberFormatException e) {
                this.view.displayError("Invalid ID, try again")
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
            this.view.displaySuccess("Performance with ID " + String.valueOf(selectedPerformanceID) + " successfully found") 
        }
    }

    private Event getEventByID(long eventID) {
        for (Event e : events) {
            if (eventID.equals(e.getEventID())) {
                return e;
            }
        }
    }

    private Event getEventByTitle(String title) {
        for (Event e : events) {
            if (title.equals(e.getTitle())) {
                return e;
            }
        }
    }

    private Performance getPerformanceByID(long performanceID) {
        for (Event e : events) {
            List<Performance> performances = e.getPerformances();
            for (Performance p : performances) {
                if (performanceID.equals(p.getPerformanceID())) {
                    return p;
                }
            }
        }
        return null;
    }
}