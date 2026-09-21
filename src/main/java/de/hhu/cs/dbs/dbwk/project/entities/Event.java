package de.hhu.cs.dbs.dbwk.project.entities;

public class Event {
    private int eventid;
    private int wohnortid;
    private String titel;
    private String beschreibung;
    private String startdatum;
    private String enddatum;
    private double kosten;

    public Event(int eventid, int wohnortid, String titel, String beschreibung, String startdatum, String enddatum, double kosten) {
        this.eventid = eventid;
        this.wohnortid = wohnortid;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.startdatum = startdatum;
        this.enddatum = enddatum;
        this.kosten = kosten;
    }

    // Getters and setters for all properties
    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public int getWohnortid() {
        return wohnortid;
    }

    public void setWohnortid(int wohnortid) {
        this.wohnortid = wohnortid;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getStartdatum() {
        return startdatum;
    }

    public void setStartdatum(String startdatum) {
        this.startdatum = startdatum;
    }

    public String getEnddatum() {
        return enddatum;
    }

    public void setEnddatum(String enddatum) {
        this.enddatum = enddatum;
    }

    public double getKosten() {
        return kosten ;
    }

    public void setKosten(double kosten) {
        this.kosten = kosten ;
    }
}
