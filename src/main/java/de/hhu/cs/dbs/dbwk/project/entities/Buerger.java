package de.hhu.cs.dbs.dbwk.project.entities;


public class Buerger {
    private int buergerid;
    private int wohnortid;
    private String berufbezeichnung;
    private String email;
    private String passwort;
    private String vorname;
    private String nachname;

    public Buerger(int buergerid, int wohnortid, String berufbezeichnung, String email, String passwort, String vorname, String nachname) {
        this.buergerid = buergerid;
        this.wohnortid = wohnortid;
        this.berufbezeichnung = berufbezeichnung;
        this.email = email;
        this.passwort = passwort;
        this.vorname = vorname;
        this.nachname = nachname;
    }
    // Constructor, getters and setters

    public int getBuergerid() {
        return buergerid;
    }

    public void setBuergerid(int buergerid) {
        this.buergerid = buergerid;
    }


    public int getWohnortid() {
        return wohnortid;
    }

    public void setWohnortid(int wohnortid) {
        this.wohnortid = wohnortid;
    }

    public String getBerufbezeichnung() {
        return berufbezeichnung;
    }

    public void setBerufbezeichnung(String berufbezeichnung) {
        this.berufbezeichnung = berufbezeichnung;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
}
