package de.hhu.cs.dbs.dbwk.project.entities;

public class Beruf {
    private int berufId;
    private String berufName;

    // Constructor, getters and setters

    public Beruf(int berufId, String berufName) {
        this.berufId = berufId;
        this.berufName = berufName;
    }

    public int getBerufId() {
        return berufId;
    }

    public void setBerufId(int berufId) {
        this.berufId = berufId;
    }

    public String getBerufName() {
        return berufName;
    }

    public void setBerufName(String berufName) {
        this.berufName = berufName;
    }

}
