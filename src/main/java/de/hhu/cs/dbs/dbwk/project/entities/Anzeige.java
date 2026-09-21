package de.hhu.cs.dbs.dbwk.project.entities;

public class Anzeige {
    private int anzeigeid;
    private String buergerid;
    private int nachrichtenAnzahl;
    private String titel;
    private String beschreibung;

    // Konstruktor, Getter und Setter
    public Anzeige(int anzeigeid, String buergerid, int nachrichtenAnzahl, String titel, String beschreibung) {
        this.anzeigeid = anzeigeid;
        this.buergerid = buergerid;
        this.nachrichtenAnzahl = nachrichtenAnzahl;
        this.titel = titel;
        this.beschreibung = beschreibung;
    }

    public Anzeige() {

    }


    public int getAnzeigeid() {
        return anzeigeid;
    }

    public void setAnzeigeid(int anzeigeid) {
        this.anzeigeid = anzeigeid;
    }

    public String getBuergerid() {
        return buergerid;
    }

    public void setBuergerid(String buergerid) {
        this.buergerid = buergerid;
    }

    public int getNachrichtenAnzahl() {
        return nachrichtenAnzahl;
    }

    public void setNachrichtenAnzahl(int nachrichtenAnzahl) {
        this.nachrichtenAnzahl = nachrichtenAnzahl;
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
}
