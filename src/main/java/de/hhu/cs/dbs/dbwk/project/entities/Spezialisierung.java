package de.hhu.cs.dbs.dbwk.project.entities;

public class Spezialisierung {
    private int spezialisierungid;
    private String bezeichnung;

    public Spezialisierung(int spezialisierungid, String bezeichnung) {
        this.spezialisierungid = spezialisierungid;
        this.bezeichnung = bezeichnung;
    }

    // Getter und Setter
    public int getSpezialisierungid() {
        return spezialisierungid;
    }

    public void setSpezialisierungid(int spezialisierungid) {
        this.spezialisierungid = spezialisierungid;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
}