package de.hhu.cs.dbs.dbwk.project.entities;
public class EventMitBewertung {
    private int eventID;
    private String titel;
    private String beschreibung;
    private String startdatum;
    private String enddatum;
    private Double kosten;
    private Integer avgBewertung;
    public EventMitBewertung() {
        // Standardkonstruktor
    }

    // Konstruktor mit allen Parametern, falls benötigt
    public EventMitBewertung( String titel, String beschreibung, String startdatum, String enddatum, Double kosten,int eventID, Integer avgBewertung) {
        this.eventID = eventID;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.startdatum = startdatum;
        this.enddatum = enddatum;
        this.kosten = kosten;
        this.avgBewertung = avgBewertung;
    }

    // Getter und Setter für alle Felder
    public int getEventID() { return eventID; }
    public void setEventID(int eventID) { this.eventID = eventID; }
    public String getTitel() { return titel; }
    public void setTitel(String titel) { this.titel = titel; }
    public String getBeschreibung() { return beschreibung; }
    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }
    public String getStartdatum() { return startdatum; }
    public void setStartdatum(String startdatum) { this.startdatum = startdatum; }
    public String getEnddatum() { return enddatum; }
    public void setEnddatum(String enddatum) { this.enddatum = enddatum; }
    public Double getKosten() { return kosten; }
    public void setKosten(Double kosten) { this.kosten = kosten; }
    public Integer getAvgBewertung() { return avgBewertung; }
    public void setAvgBewertung(Integer avgBewertung) { this.avgBewertung = avgBewertung; }
}
