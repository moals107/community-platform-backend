package de.hhu.cs.dbs.dbwk.project.entities;

public class Gruppe {
    private int gruppeid;
    private String bezeichnung;
    private String sprache;
    private boolean istPrivat;

    // Konstruktor, Getter und Setter
    public Gruppe(int gruppeid, String bezeichnung, String sprache, boolean istPrivat) {
        this.gruppeid = gruppeid;
        this.bezeichnung = bezeichnung;
        this.sprache = sprache;
        this.istPrivat = istPrivat;
    }

    public int getGruppeid() {
        return gruppeid;
    }

    public void setGruppeid(int gruppeid) {
        this.gruppeid = gruppeid;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public boolean isIstPrivat() {
        return istPrivat;
    }

    public void setIstPrivat(boolean istPrivat) {
        this.istPrivat = istPrivat;
    }
}
