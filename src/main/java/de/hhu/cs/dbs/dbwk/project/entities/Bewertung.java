package de.hhu.cs.dbs.dbwk.project.entities;

public class Bewertung {
    private String buergerEmail;
    private int eventID;
    private int punktzahl;

    public Bewertung(String buergerEmail, int eventID, int punktzahl) {
        this.buergerEmail = buergerEmail;
        this.eventID = eventID;
        this.punktzahl = punktzahl;
    }

    // Getter und Setter


    // Getter und Setter
    public String getBuergerEmail() { return buergerEmail; }
    public void setBuergerEmail(String buergerEmail) { this.buergerEmail = buergerEmail; }
    public int getEventID() { return eventID; }
    public void setEventID(int eventID) { this.eventID = eventID; }
    public int getPunktzahl() { return punktzahl; }
    public void setPunktzahl(int punktzahl) { this.punktzahl = punktzahl; }
}
