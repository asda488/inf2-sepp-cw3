package com.acmecorp.events.Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.acmecorp.events.Models.Booking;
import com.acmecorp.events.Models.EntertainmentProvider;
import com.acmecorp.events.Models.Event;
import com.acmecorp.events.Models.EventType;
import com.acmecorp.events.Models.Performance;
import com.acmecorp.events.Models.Student;
import com.acmecorp.events.Models.User;
import com.acmecorp.events.Services.PaymentSystem;
import com.acmecorp.events.Views.View;

public class EventPerformanceController extends Controller{
    private long nextEventID;
    private long nextPerformanceID;
    private PaymentSystem paymentSystem;
    public List<Event> events;

    public EventPerformanceController(View view, User currentUser, PaymentSystem paymentSystem) {
        super(view, currentUser);
        this.nextEventID = 0;
        this.nextPerformanceID = 0;
        this.events = new ArrayList<>();
        this.paymentSystem = paymentSystem;
    }

    public EventPerformanceController(View view, User currentUser, List<Event> events, PaymentSystem paymentSystem) {
        super(view, currentUser);
        this.nextEventID = 0;
        this.nextPerformanceID = 0;
        this.events = events;
        this.paymentSystem = paymentSystem;
    }

    public Event createEvent() {
        if (!checkCurrentUserIsEntertainmentProvider()) {
            this.view.displayError("Only EPs can create events");
            return null;
        }

        String title;
        while (true) {
            title = this.view.getInput("Enter event title: ").trim();
            if (!title.isEmpty()) {
                break; //valid input causes loop exit
            }
            this.view.displayError("Invalid input, try again.");
        }

        EventType type;
        while (true) {
            String typeInput = this.view.getInput("Enter event type (Music/Theatre/Dance/Movie/Sports): ").trim();
            try {
                type = EventType.valueOf(typeInput.toUpperCase());
                break; // valid input causes loop exit
            } catch (IllegalArgumentException e) {
                this.view.displayError("Invalid type, try again.");
            }
        }

        boolean isTicketed;
        while (true) {
            String ticketedInput = this.view.getInput("Is the event ticketed (yes/no): ").trim();
            isTicketed = ticketedInput.equalsIgnoreCase("yes");
            if (ticketedInput.equalsIgnoreCase("yes") || ticketedInput.equalsIgnoreCase("no")) {
                break; // valid input causes loop exit
            }
            this.view.displayError("Invalid choice, try again.");
        }

        int noOfPerformances;
        while (true) {
            try {
                noOfPerformances = Integer.parseInt(this.view.getInput("Enter number of performances: "));
                if (noOfPerformances > 0) {
                    break; // valid input causes loop exit
                }
                this.view.displayError("Invalid number, try again.");
            } catch (NumberFormatException e) {
                this.view.displayError("Invalid number, try again.");
            }
        }
        List<Performance> performances = new ArrayList<>();

        Event event = new Event(nextEventID, title, type, isTicketed, performances, (EntertainmentProvider) currentUser);
        events.add(event);
        nextEventID++;

        for (int i = 1; i <= noOfPerformances; i++) {
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;
            while (true) {
                try {
                    startDateTime = LocalDateTime.parse(this.view.getInput("Performance " + i + 
                        "\nEnter the start date and time (yyyy-MM-ddTHH:mm): "));
                    endDateTime = LocalDateTime.parse(this.view.getInput("Enter the end date and time (yyyy-MM-ddTHH:mm): "));
                    if (endDateTime.isAfter(startDateTime) && !event.hasPerformancesAtSameTime(startDateTime, endDateTime)) {
                        break;
                    } else if (!endDateTime.isAfter(startDateTime)) {
                        this.view.displayError("Invalid end time, try again.");
                    } else {
                        this.view.displayError("Overlapping performance times, try again.");
                    }
                } catch (DateTimeParseException e) {
                    this.view.displayError("Invalid input, try again.");
                } 
            }

            String performersInput;
            while (true) {
                performersInput = this.view.getInput("Enter name(s) of the performer(s) (comma-separated): ").trim();
                if (!performersInput.isEmpty()) {
                    break; // valid input causes loop exit
                }
                this.view.displayError("Invalid string, try again.");
            }
            List<String> performerNames = Arrays.stream(performersInput.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

            double ticketPrice;
            int numTickets;
            if (isTicketed) {
                while (true) {
                    try {
                        ticketPrice = Double.parseDouble(this.view.getInput("Enter the ticket price: £"));
                        break; // valid input causes loop exit
                    } catch (NumberFormatException e) {
                        this.view.displayError("Invalid price, try again.");
                    }
                }
                while (true) {
                    try {
                        numTickets = Integer.parseInt(this.view.getInput("Enter the remaining number of tickets: "));
                        if (numTickets > 0) {
                            break; //valid input causes loop exit
                        }
                        this.view.displayError("Invalid number, try again.");
                    } catch (NumberFormatException e) {
                        this.view.displayError("Invalid number, try again.");
                    }
                }
            } else {
                ticketPrice = 0;
                numTickets = 0;
            }

            String venueAddress;
            while (true) {
                venueAddress = this.view.getInput("Enter the venue address: ").trim();
                if (!venueAddress.isEmpty()) {
                    break; //valid input causes loop exit
                }
                this.view.displayError("Invalid input, try again.");
            }

            int venueCapacity;
            while (true) {
                try {
                    venueCapacity = Integer.parseInt(this.view.getInput("Enter the venue capacity: "));
                    if (venueCapacity > 0) {
                        break; //valid input causes loop exit
                    }
                } catch (NumberFormatException e) {
                    this.view.displayError("Invalid number, try again.");
                }
            }

            boolean venueIsOutdoors;
            while (true) {
                String outdoorsInput = this.view.getInput("Is the venue outdoors (yes/no): ").trim();
                venueIsOutdoors = outdoorsInput.equalsIgnoreCase("yes");
                if (outdoorsInput.equalsIgnoreCase("yes") || outdoorsInput.equalsIgnoreCase("no")) {
                    break; // valid input causes loop exit
                }
                this.view.displayError("Invalid choice, try again.");
            }

            boolean venueAllowsSmoking;
            while (true) {
                String smokingInput = this.view.getInput("Does the venue allow smoking (yes/no): ").trim();
                venueAllowsSmoking = smokingInput.equalsIgnoreCase("yes");
                if (smokingInput.equalsIgnoreCase("yes") || smokingInput.equalsIgnoreCase("no")) {
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
        LocalDate searchDate = null;
        while (true) {
            try {
                searchDate = LocalDate.parse(this.view.getInput("Enter search date for performances (yyyy-MM-dd): "));
            } catch (DateTimeParseException e) {
                this.view.displayError("Invalid date, try again.");
                continue;
            }
            if (!searchDate.isBefore(LocalDate.now())) {
                break; // valid input causes loop exit
            }
            this.view.displayError("Invalid date, try again.");
        }

        List<String> performancesOnDate = new ArrayList<>();
        for (Event e : events) {
            Collection<String> performances = e.getInfoForPerformancesOnDate(searchDate);
            performancesOnDate.addAll(performances);
        }

        if (performancesOnDate.isEmpty()) {
            this.view.displayError("There are no performances on " + searchDate.toString());
            return;
        }

        List<String> filteredPerformancesOnDate = new ArrayList<>();
        List<String> preferences;
        if (checkCurrentUserIsStudent()) {
            List<String> filteredPerformancesOnDate = new ArrayList<>();
            String preferences;
            if (checkCurrentUserIsStudent()) {
                preferences = ((Student)currentUser).getPreferences().toString();
                for (String s : performancesOnDate) {
                    int indexStart = s.indexOf("Type = ");
                    int indexEnd = s.indexOf(", Ticketed");
                    String type = s.substring(indexStart+7, indexEnd);
                    if (preferences.contains(type)) {
                        filteredPerformancesOnDate.add(s);
                    }
                }
                performancesOnDate = filteredPerformancesOnDate;
            }
        }

        this.view.displayListofPerformances(performancesOnDate);
        this.view.displaySuccess("Performances on " + searchDate.toString() + "successfully found");
    }

    public void viewPerformance() {
        long selectedPerformanceID;
        while (true) {
            try {
                selectedPerformanceID = Long.parseLong(this.view.getInput("Enter PerformanceID: "));
                break; // valid input exits loop
            } catch (NumberFormatException e) {
                this.view.displayError("Invalid ID, try again.");
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

    public void cancelPerformance() {
        assert this.checkCurrentUserIsEntertainmentProvider();
        EntertainmentProvider ep = (EntertainmentProvider) this.currentUser;

        Performance p = null;
        while (p == null) {
            long id = readLong("Enter ID of the performance to cancel");
            p = getPerformanceByID(id);
            if (p == null) {
                this.view.displayError("Performance with given number does not exist.");
                continue;
            }
            if (!p.checkCreatedByEP(ep.getEmail())) {
                this.view.displayError("Performance was not created by the current entertainment provider.");
                p = null;
                continue;
            }
            if (p.getStatus() == Performance.PerformanceStatus.CANCELLED) {
                this.view.displayError("Performance is already cancelled");
                p = null;
                continue;
            }
            if (!p.checkHasNotHappenedYet()) {
                this.view.displayError("Performance has already happened.");
                p = null;
            }
        }

        String msg = "";
        while (msg.isBlank()) {
            msg = this.view.getInput("Enter a message for affected students")
                .trim();
            if (msg.isBlank()) {
                this.view.displayError("Message cannot be empty.");
            }
        }

        List<Booking> list = new ArrayList<>();
        for (Booking b : p.getBookings()) {
            if (b.getStatus() == Booking.BookingStatus.ACTIVE) {
                list.add(b);
            }
        }

        for (Booking b : list) {
            Student s = b.getStudent();
            boolean ok = this.paymentSystem.processRefund(b.getNumTickets(),
                p.getEventTitle(), s.getEmail(), s.getPhoneNumber(),
                ep.getEmail(), b.getAmountPaid(), msg);
            if (!ok) {
                this.view.displayError("There was an issue with a refund. The performance cannot be cancelled.");
                return;
            }
        }

        for (Booking b : list) {
            b.cancelByProvider();
        }
        p.cancel();
        this.view.displaySuccess("Cancellation Successful!");
    }

    private Event getEvtByID(long evtId) {
        for (Event e : this.events) {
            if (evtId == e.getEventID()) {
                return e;
            }
        }
        return null;
    }

    private Event getEventByTitle(String title) {
        for (Event e : this.events) {
            if (title.equals(e.getTitle())) {
                return e;
            }
        }
        return null;
    }

    private Performance getPerformanceByID(long performanceID) {
        for (Event e : this.events) {
            List<Performance> performances = e.getPerformances();
            for (Performance p : performances) {
                if (performanceID == p.getPerformanceID()) {
                    return p;
                }
            }
        }
        return null;
    }

    private long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(this.view.getInput(prompt));
            } catch (NumberFormatException e) {
                this.view.displayError("Invalid number try again");
            }
        }
    }
}
