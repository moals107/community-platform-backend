package de.hhu.cs.dbs.dbwk.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Nachricht {
    @JsonIgnore
    private int nachrichtID;

    private int anzeigeid;
    private String buergerid;
    private String text;

    public Nachricht(int anzeigeid, String buergerid, String text) {
        this.anzeigeid = anzeigeid;
        this.buergerid = buergerid;
        this.text = text;
    }
    public Nachricht(int nachrichtID, int anzeigeid, String buergerid, String text) {
        this.nachrichtID = nachrichtID;
        this.anzeigeid = anzeigeid;
        this.buergerid = buergerid;
        this.text = text;
    }

    public Nachricht(){}

    public int getNachrichtID() {
        return nachrichtID;
    }

    public void setNachrichtID(int nachrichtID) {
        this.nachrichtID = nachrichtID;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
