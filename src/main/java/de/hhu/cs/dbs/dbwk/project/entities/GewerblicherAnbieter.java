package de.hhu.cs.dbs.dbwk.project.entities;


public class GewerblicherAnbieter {
    private int anbieterid;
    private int buergerid;
    private String berufbezeichnung;
    private int gruendungsjahr;
    private String email;
    private String passwort;
    private String vorname;
    private String nachname;

    public GewerblicherAnbieter(int anbieterid,int buergerid,
                                String berufbezeichnung, int gruendungsjahr,String email,
                                String passwort,String vorname,String nachname){
        this.anbieterid = anbieterid;
        this.buergerid = buergerid;
        this.berufbezeichnung = berufbezeichnung;
        this.gruendungsjahr = gruendungsjahr;
        this.email = email;
        this.passwort = passwort;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public GewerblicherAnbieter(){}

    public int getAnbieterid() {
        return anbieterid;
    }

    public void setAnbieterid(int anbieterid) {
        this.anbieterid = anbieterid;
    }

    public int getBuergerid() {
        return buergerid;
    }

    public void setBuergerid(int buergerid) {
        this.buergerid = buergerid;
    }

    public String getBerufbezeichnung() {
        return berufbezeichnung;
    }

    public void setBerufbezeichnung(String berufbezeichnung) {
        this.berufbezeichnung = berufbezeichnung;
    }

    public int getGruendungsjahr() {
        return gruendungsjahr;
    }

    public void setGruendungsjahr(int gruendungsjahr) {
        this.gruendungsjahr = gruendungsjahr;
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


/*
public class GewerblicherAnbieter extends Buerger {
    private int gewerblicheranbieterid;
    private int gruendungsjahr;
    private String BuergerEmail;

    public GewerblicherAnbieter(int buergerid, int wohnortid, String berufbezeichnung,int gruendungsjahr,
                                String email, String passwort, String vorname, String nachname
                                ) {
        super(buergerid, wohnortid, berufbezeichnung, email, passwort, vorname, nachname);
        this.BuergerEmail = email;
        this.gruendungsjahr = gruendungsjahr;
    }

    // Getter and setter for gruendungsjahr
    public int getGruendungsjahr() {
        return gruendungsjahr;
    }

    public void setGruendungsjahr(int gruendungsjahr) {
        this.gruendungsjahr = gruendungsjahr;
    }

    public String getBuergerEmail() {
        return BuergerEmail;
    }

    public void setBuergerEmail(String buergerEmail) {
        BuergerEmail = buergerEmail;
    }

    public int getGewerblicheranbieterid() {
        return gewerblicheranbieterid;
    }

    public void setGewerblicheranbieterid(int gewerblicheranbieterid) {
        this.gewerblicheranbieterid = gewerblicheranbieterid;
    }
}

 */
