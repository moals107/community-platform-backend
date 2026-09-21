package de.hhu.cs.dbs.dbwk.project.controller;


import de.hhu.cs.dbs.dbwk.project.entities.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private final Repository repository;

    private final JdbcTemplate jdbcTemplate;


    public Service(Repository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Buerger> getBuergerByPlz(String plz) {
        return repository.findBuergerByPlz(plz);
    }

    public Buerger addBuerger(Buerger buerger) {
        return repository.saveBuerger(buerger);
    }

    public List<GewerblicherAnbieter> getGewerblicheAnbieterByEmail(String email) {
        return repository.findGewerblicheAnbieterByEmail(email);
    }




    public GewerblicherAnbieter addGewerblicherAnbieter(GewerblicherAnbieter anbieter, Buerger buerger) {
        return repository.saveGewerblicherAnbieter(anbieter, buerger);
    }




    public List<Wohnort> getWohnorteByStrasse(String strasse) {
        return repository.findWohnorteByStrasse(strasse);
    }

    public Wohnort addWohnort(Wohnort wohnort) {
        return repository.saveWohnort(wohnort);
    }

    public List<Spezialisierung> findSpezialisierungenByBezeichnung(String bezeichnung) {
        return repository.findSpezialisierungenByBezeichnung(bezeichnung);
    }

    public Spezialisierung addSpezialisierung(String bezeichnung) throws DataIntegrityViolationException, DataAccessException {
        return repository.addSpezialisierung(bezeichnung);
    }

    public List<Event> findEvents(String titel, Boolean mehrtaegig) {
        return repository.getEvents(titel, mehrtaegig);
    }

    public List<Gruppe> findGruppenByBezeichnung(String bezeichnung) {
        return repository.findGruppenByBezeichnung(bezeichnung);
    }


    public List<Anzeige> findAnzeigenByNachrichtenAnzahl(Integer nachrichtenAnzahl) {
        return repository.getAnzeigen(nachrichtenAnzahl);
    }

    public List<Nachricht> findNachrichten(Integer anzeigeid) {
        return repository.getNachrichten(anzeigeid);
    }

    public List<EventMitBewertung> findEventsMitDurchschnittsbewertung(Integer minGrenze) {
        return repository.findEventsMitDurchschnittsbewertung(minGrenze);
    }

    public Event addEvent(Event event, String veranstalterEmail) {
        if (!repository.checkWohnortAndVeranstalterExists(event.getWohnortid(), veranstalterEmail)) {
            throw new IllegalArgumentException("Invalid wohnortID or veranstalterEmail: Wohnort or Veranstalter does not exist.");
        }
        return repository.saveEvent(event, veranstalterEmail);
    }

    @Transactional
    public Gruppe addGruppe(Gruppe gruppe, String moderatorEmail) {
        if (!repository.buergerExists(moderatorEmail)) {
            throw new IllegalArgumentException("Moderator does not exist.");
        }
        return repository.saveGruppe(gruppe, moderatorEmail);
    }

    public Anzeige addAnzeige(Anzeige anzeige) {
        return repository.saveAnzeige(anzeige);
    }

    public Nachricht addNachricht(Nachricht nachricht) {
        return repository.saveNachricht(nachricht);
    }


    public Anzeige updateAnzeige(int anzeigeid, String userEmail, String titel, String beschreibung) {
        if (!repository.isOwner(anzeigeid, userEmail)) {
            throw new SecurityException("User is not the owner of the advertisement.");
        }

        return repository.updateAnzeige(anzeigeid, titel, beschreibung);
    }



    public boolean anzeigeHasNachrichten(int anzeigeid) {
        return repository.countNachrichtenByAnzeigeId(anzeigeid) > 0;
    }

    public void deleteAnzeige(int anzeigeid) {
        repository.deleteAnzeigeById(anzeigeid);
    }

    public boolean isOwner(int anzeigeid, String userEmail) {
        return repository.isOwner(anzeigeid, userEmail);
    }
    public boolean anzeigeExists(int anzeigeid) {
        return repository.anzeigeExists(anzeigeid);
    }



    public boolean nachrichtExists(int anzeigeid, int nachrichtid) {
        return repository.nachrichtExists(anzeigeid, nachrichtid);
    }

    public boolean isAuthorOfNachricht(int nachrichtid, String userEmail) {
        return repository.isAuthorOfNachricht(nachrichtid, userEmail);
    }

    public void deleteNachricht(int nachrichtid) {
        repository.deleteNachrichtById(nachrichtid);
    }

    public void addEventBewertung(int eventID, String userEmail, int punktzahl) throws Exception {
        if (!hasUserAttendedEvent(userEmail, eventID)) {
            throw new Exception("Benutzer hat nicht am Event teilgenommen.");
        }
        // Füge die Bewertung hinzu, wenn der Benutzer teilgenommen hat
        String sql = "INSERT INTO Bewertet (EventID, BuergerEmail, Skala) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, eventID, userEmail, punktzahl);
    }

    private boolean hasUserAttendedEvent(String userEmail, int eventID) {
        String sql = "SELECT COUNT(*) FROM Besuchen WHERE BuergerEmail = ? AND EventID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userEmail, eventID}, Integer.class);
        return count != null && count > 0;
    }

}
