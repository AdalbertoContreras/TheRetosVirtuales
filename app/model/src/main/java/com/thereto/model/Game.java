package com.thereto.model;

public class Game {
    public String name_challeng = "";
    public int accumulated_tickets = 0;
    public String date_limit = "";
    public String date_creation = "";
    public String description = "";

    public Game() {

    }

    public Game(String title, int tickect, String date) {
        this.name_challeng = title;
        this.accumulated_tickets = tickect;
        this.date_limit = date;
    }
}
