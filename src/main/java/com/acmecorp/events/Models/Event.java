package com.acmecorp.events.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Event {
    public static enum EventType {
        MUSIC,
        THEATRE,
        DANCE,
        MOVIE,
        SPORTS
    }

    private long eventID;
    private String title;
    private EventType type;
    private boolean isTicketed;
    private List<Performance> performances;
    private EntertainmentProvider organiser;

    public Event(long eventID, String title, EventType type, boolean isTicketed, 
        List<Performance> performances, EntertainmentProvider organiser) {
        this.eventID = eventID;
        this.title = title;
        this.type = type;
        this.isTicketed = isTicketed;
        this.performances = performances;
        this.organiser = organiser;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public boolean getTicketed() {
        return isTicketed;
    }

    public void setTicketed(boolean isTicketed) {
        this.isTicketed = isTicketed;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public EntertainmentProvider getOrganiser() {
        return organiser;
    }

    public Performance createPerformance(long performanceID, LocalDateTime startDateTime, LocalDateTime endDateTime, 
        Collection<String> performerNames, double ticketPrice, int numTickets, String venueAddress, int venueCapacity, 
        boolean venueIsOutdoors, boolean venueAllowsSmoking) {
        Performance performance = new Performance(this, performanceID, startDateTime, endDateTime, 
            performerNames, ticketPrice, numTickets, venueAddress, venueCapacity, 
            venueIsOutdoors, venueAllowsSmoking);
        return performance;
    }

    public Performance getPerformanceByID(long performanceID) {
        for (Performance performance : performances) {
            if (performance.getPerformanceID() == performanceID) {
                return performance;
            }
        }
        throw new IllegalArgumentException("Performance with ID " + performanceID + " not found");
    }

    public Collection<String> getInfoForPerformancesOnDate(LocalDateTime searchDateTime) {
        List<String> performancesOnDate = new ArrayList<>();
        for (Performance performance : performances) {
            if (performance.getStartDateTime().toLocalDate().equals(searchDateTime.toLocalDate())) {
                performancesOnDate.add(performance.toString());
            }
        }
        return performancesOnDate;
    }

    private String getOrganiserName() {
        return this.organiser.getOrgName();
    }

    public String getOrganiserEmail() {
        return this.organiser.getEmail();
    }
    
    public double getAverageRatingOfPerformances() {
        return 0.0;
        //TODO
    }

    public boolean hasPerformancesAtSameTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return true;
        //TODO;
    }

    @Override
    public String toString() {
        return ("EventID = " + eventID + ", Title = " + title + ", Type = " + type + "Ticketed = " + isTicketed);
    }
}