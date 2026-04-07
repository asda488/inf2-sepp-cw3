package com.acmecorp.events.Models;

import java.util.HashSet;
import java.util.Set;

public class StudentPreferences {
    private boolean music;
    private boolean theatre;
    private boolean dance;
    private boolean movie;
    private boolean sports;

    public boolean updatePreferences(String raw) {
        if (raw == null) {
            return false;
        }
        if (raw.isBlank()) {
            clear();
            return true;
        }

        Set<String> vals = new HashSet<>();
        for (String part : raw.split(",")) {
            String s = part.trim().toLowerCase();
            if (s.isEmpty()) {
                return false;
            }
            switch (s) {
                case "music" -> vals.add("music");
                case "theatre", "theater" -> vals.add("theatre");
                case "dance" -> vals.add("dance");
                case "movie", "movies" -> vals.add("movie");
                case "sport", "sports" -> vals.add("sports");
                default -> {
                    return false;
                }
            }
        }

        this.music = vals.contains("music");
        this.theatre = vals.contains("theatre");
        this.dance = vals.contains("dance");
        this.movie = vals.contains("movie");
        this.sports = vals.contains("sports");
        return true;
    }

    private void clear() {
        this.music = false;
        this.theatre = false;
        this.dance = false;
        this.movie = false;
        this.sports = false;
    }

    public boolean isPreferMusicEvents() {
        return music;
    }

    public boolean isPreferTheatreEvents() {
        return theatre;
    }

    public boolean isPreferTheaterEvents() {
        return theatre;
    }

    public boolean isPreferDanceEvents() {
        return dance;
    }

    public boolean isPreferMovieEvents() {
        return movie;
    }

    public boolean isPreferSportsEvents() {
        return sports;
    }

    @Override
    public String toString(){
        return ((music) ? "music " : "")
            + ((theatre) ? "theatre theater " : "")
            + ((dance) ? "dance " : "")
            + ((movie) ? "movie " : "")
            + ((sports) ? "sports " : "");
    }
}
