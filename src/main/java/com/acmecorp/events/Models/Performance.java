package com.acmecorp.events.Models;
import com.acmecorp.events.Services.MockPaymentSystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.*;

public class Performance {
    private long performanceID;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Collection<String> performerNames;
    private String venueAddress;
    private int venueCapacity;
    private boolean venueIsOutdoors;
    private boolean venueAllowsSmoking;
    private int numTicketsTotal;
    private int numTicketsSold;
    private double ticketPrice;
    private boolean isSponsored;
    private double sponsoredAmount;
    private Collection<Integer> reviewRatings;
    private Collection<String> reviewComments;
    private PerformanceStatus status;
    private Event event;
    private Collection<MockPaymentSystem.Booking> bookings;
    
    enum PerformanceStatus {
        ACTIVE,
        CANCELLED
    }

    public Performance(Event event, long performanceID, LocalDateTime startDateTime, LocalDateTime endDateTime, Collection<String> performerNames, double ticketPrice, int numTickets, String venueAddress, int venueCapacity, boolean venueIsOutdoors, boolean venueAllowsSmoking) {
        this.event = event;
        this.performanceID = performanceID;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames;
        this.ticketPrice = ticketPrice;
        this.numTicketsTotal = numTickets;
        this.numTicketsSold = 0;
        this.venueAddress = venueAddress;
        this.venueCapacity = venueCapacity;
        this.venueIsOutdoors = venueIsOutdoors;
        this.venueAllowsSmoking = venueAllowsSmoking;
        this.isSponsored = false;
        this.sponsoredAmount = 0;
        this.reviewRatings = new ArrayList<>();
        this.reviewComments = new ArrayList<>();
        this.status = PerformanceStatus.ACTIVE;
        this.bookings = new ArrayList<>();
    }

    public long getPerformanceID() {
        return performanceID;
    }

    public void setPerformanceID(long performanceID) {
        this.performanceID = performanceID;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Collection<String> getPerformerNames() {
        return performerNames;
    }

    public void setPerformerNames(Collection<String> performerNames) {
        this.performerNames = performerNames;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public int getVenueCapacity() {
        return venueCapacity;
    }

    public void setVenueCapacity(int venueCapacity) {
        this.venueCapacity = venueCapacity;
    }

    public boolean getVenueIsOutdoors() {
        return venueIsOutdoors;
    }

    public void setVenueIsOutdoors(boolean venueIsOutdoors) {
        this.venueIsOutdoors = venueIsOutdoors;
    }

    public boolean getVenueAllowsSmoking() {
        return venueAllowsSmoking;
    }

    public void setVenueAllowsSmoking(boolean venueAllowsSmoking) {
        this.venueAllowsSmoking = venueAllowsSmoking;
    }

    public int getNumTicketsTotal() {
        return numTicketsTotal;
    }

    public void setNumTicketsTotal(int numTicketsTotal) {
        this.numTicketsTotal = numTicketsTotal;
    }

    public int getNumTicketsSold() {
        return numTicketsSold;
    }

    public void setNumTicketsSold(int numTicketsSold) {
        this.numTicketsSold = numTicketsSold;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public boolean getIsSponsored() {
        return isSponsored;
    }

    public void setIsSponsored(boolean sponsored) {
        isSponsored = sponsored;
    }

    public double getSponsoredAmount() {
        return sponsoredAmount;
    }

    public void setSponsoredAmount(double sponsoredAmount) {
        this.sponsoredAmount = sponsoredAmount;
    }

    public Collection<Integer> getReviewRatings() {
        return reviewRatings;
    }

    public void setReviewRatings(Collection<Integer> reviewRatings) {
        this.reviewRatings = reviewRatings;
    }

    public Collection<String> getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(Collection<String> reviewComments) {
        this.reviewComments = reviewComments;
    }

    public PerformanceStatus getStatus() {
        return status;
    }

    public void setStatus(PerformanceStatus status) {
        this.status = status;
    }

    public Event getEvent() {
        return event;
    }

    public Collection<MockPaymentSystem.Booking> getBookings() {
        return bookings;
    }

    public void cancel() {
        this.status = PerformanceStatus.CANCELLED;
    }

    public boolean checkIfEventIsTicketed() {
        return event.getTicketed();
    }

    public boolean checkIfTicketsLeft(int numTicketsToBuy) {
        return (numTicketsTotal-(numTicketsSold+numTicketsToBuy) >= 0);
    }

    public double getFinalTicketPrice() {
        if (isSponsored) {
            return ticketPrice - Math.floor(sponsoredAmount/(numTicketsTotal-numTicketsSold)*100)/100;
        } else {
            return ticketPrice;
        }
    }

    public String getOrganiserEmail() {
        EntertainmentProvider organiser = event.getOrganiser();
        return organiser.getOrgEmail();
    }

    public String getEventTitle() {
        return event.getTitle();
    }

    public boolean checkHasNotHappenedYet() {
        return this.endDateTime.isAfter(LocalDateTime.now());
    }

    public String toString() {
        return ("Event = " + event.getTitle() + 
        ", PerformanceID = " + String.valueOf(performanceID) + 
        ", Start Date and Time = " + startDateTime.toString() + 
        ", End Date and Time = " + endDateTime.toString() + 
        ", Performers = " + performerNames + 
        ", Price = " + String.valueOf(ticketPrice) + 
        ", No. of Tickets = " + String.valueOf((numTicketsTotal-numTicketsSold)) + 
        ", Venue Address = " + venueAddress + 
        ", Capacity = " + String.valueOf(venueCapacity) + 
        ", Outdoors = " + String.valueOf(venueIsOutdoors) + 
        ", Smoking = " + String.valueOf(venueAllowsSmoking) + 
        ", Status = " + String.valueOf(status));
    }
}