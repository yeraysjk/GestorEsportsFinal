package com.torneo.projectegestoresportsfinal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private String name;
    private LocalDate date;
    private String game;
    private String format;
    private String prizes;
    private List<Participant> participants;

    public Tournament(String name, LocalDate date, String game, String format, String prizes) {
        this.name = name;
        this.date = date;
        this.game = game;
        this.format = format;
        this.prizes = prizes;
        this.participants = new ArrayList<>();
    }

    public Tournament() {
        this.participants = new ArrayList<>();
    }

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPrizes() {
        return prizes;
    }

    public void setPrizes(String prizes) {
        this.prizes = prizes;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return name + " - " + date.toString();
    }
}
