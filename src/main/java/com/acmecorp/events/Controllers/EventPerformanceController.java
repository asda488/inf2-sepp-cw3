package com.acmecorp.events.Controllers;

import java.time.LocalDateTime;

import com.acmecorp.events.Models.User;
import com.acmecorp.events.Views.View;

public class EventPerformanceController extends Controller{
    private long nextEventID;
    private long nextPerformanceID;

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
    }

    public CreateEvent() {
        while (true) {
                String title = this.view.getInput("What is the name of the event?").trim();
                if (!title.empty()) {
                    break; //valid input causes loop exit
                }
                this.view.showMessage("Invalid input, try again.")
            }

        while (true) {
            String type = this.view.getInput("What is the event type? (Music/Theatre/Dance/Movie/Sports)").trim();
            try {
                EventType event = EventType.valueOf(input.toUpperCase());
                break; // valid input causes loop exit
            } catch (IllegalArgumentException e) {
                this.view.showMessage("Invalid type, try again.");
            }
        }

        while (true) {
            String ticketedInput = this.view.getInput("Is the event ticketed (yes/no):").trim();
            boolean isTicketed = ticketedInput.equalsIgnoreCase("yes");
            if ticketedInput.equalsIgnoreCase("yes") || ticketedInput.equalsIgnoreCase("no") {
                break; // valid input causes loop exit
            }
            this.view.showMessage("Invalid choice, try again.");
        }

        while (true) {
            try {
                int noOfPerformances = Integer.parseInt(this.view.getInput("Enter number of performances:"));
                break; // valid input causes loop exit
            } catch (NumberFormatException e) {
                this.view.showMessage("Invalid number, try again.");
            }
        }

        Event event = new Event(nextEventID, title, type, isTicketed, currentUser);
        nextEventID++;

        for (int i = 1; i <= noOfPerformances; i++) {
            System.out.println("Performance " + i)

            while (true) {
                try {
                    LocalDateTime startDateTime = LocalDateTime.parse(this.view.getInput("Enter performance start date and time (yyyy-MM-dd HH:mm):"));
                    break; // valid input causes loop exit
                } catch (NumberFormatException e) {
                    this.view.showMessage("Invalid input, try again.");
                }
            }

            while (true) {
                try {
                    LocalDateTime endDateTime = LocalDateTime.parse(this.view.getInput("Enter performance end date and time (yyyy-MM-dd HH:mm):"));
                    break; // valid input causes loop exit
                } catch (NumberFormatException e) {
                    this.view.showMessage("Invalid input, try again.");
                }
            }

            while (true) {
                String performersInput = this.view.getInput("Enter name(s) of the performer(s) (comma-separated):").trim();
                if (!performersInput.isEmpty()) {
                    break; // valid input causes loop exit
                }
                this.view.showMessage("Invalid string, try again.");
            }
            List<String> performerNames = Arrays.stream(performersInput.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList()); 


            while (true) {
                try {
                    double ticketPrice = Double.parseDouble(this.view.getInput("Enter the ticket price: £"));
                    break; // valid input causes loop exit
                } catch (NumberFormatException e) {
                    this.view.showMessage("Invalid price, try again.");
                }
            }

            if (isTicketed) {
                while (true) {
                    int numTickets = this.view.getInput("Enter the remaining number of tickets:")
                    if (numTickets > 0) {
                        break; //valid input causes loop exit
                    }
                    this.view.showMessage("Invalid number, try again.");
                }
            } else {
                int numTickets = null;
            }

            while (true) {
                String venueAddress = this.view.getInput("Enter the venue address:").trim();
                if (!venueAddress.empty()) {
                    break; //valid input causes loop exit
                }
                this.view.showMessage("Invalid input, try again.")
            }

            while (true) {
                int venueCapacity = this.view.getInput("Enter the venue capacity:");
                if (venueCapacity > 0) {
                    break; //valid input causes loop exit
                }
                this.view.showMessage("Invalid number, try again.");
            }

            while (true) {
                String outdoorsInput = this.view.getInput("Is the venue outdoors (yes/no):").trim();
                boolean venueIsOutdoors = outdoorsInput.equalsIgnoreCase("yes");
                if outdoorsInput.equalsIgnoreCase("yes") || outdoorsInput.equalsIgnoreCase("no") {
                    break; // valid input causes loop exit
                }
                this.view.showMessage("Invalid choice, try again.");
            }

            while (true) {
                String smokingInput = this.view.getInput("Does the venue allow smoking (yes/no):").trim();
                boolean venueAllowsSmoking = smokingInput.equalsIgnoreCase("yes");
                if smokingInput.equalsIgnoreCase("yes") || smokingInput.equalsIgnoreCase("no") {
                    break; // valid input causes loop exit
                }
                this.view.showMessage("Invalid choice, try again.");
            }

            e.createPerformance(Event event, long nextPerformanceID, LocalDateTime startDateTime, LocalDateTime endDateTime, Collection<String> performerNames, double ticketPrice, int numTickets, String venueAddress, int venueCapacity, boolean venueIsOutdoors, boolean venueAllowsSmoking);
            nextPerformanceID++;
        }
    }
}