package com.acmecorp.events.Models;

public class Event {
    private long eventID;
    private String title;
    private EventType type;
    private Boolean isTicketed;
    private List<Performance> performances;

    public Event(long eventID, String title, EventType type, Boolean isTicketed, List<Performance> performances) {
        this.eventID = eventID;
        this.title = title;
        this.type = type;
        this.isTicketed = isTicketed;
        this.performances = performances;
    }

    public long getID() {
        return eventID;
    }

    public void setID(long eventID) {
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

    public Boolean getTicketed() {
        return isTicketed;
    }

    public void setTicketed(Boolean isTicketed) {
        this.isTicketed = isTicketed;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public Performance createPerformance(int nextPerformanceID, LocalDateTime start, LocalDateTime end, List<String> performers, float price, int ticketCount, String venueAddress, int venueCapacity, boolean isOutdoors, boolean canSmoke) {
        Performance performance = new Performance(nextPerformanceID, start, end, performers, price, ticketCount, venueAddress, venueCapacity, isOutdoors, canSmoke);
    }
}