package de.hhu.cs.dbs.dbwk.project.controller;


import de.hhu.cs.dbs.dbwk.project.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Repository
public class Repository {
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;


    public Repository(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }


    private RowMapper<Buerger> buergerRowMapper = (rs, rowNum) -> new Buerger(
            rs.getInt("buergerid"),
            rs.getInt("wohnortid"),
            rs.getString("berufbezeichnung"),
            rs.getString("email"),
            rs.getString("passwort"),
            rs.getString("vorname"),
            rs.getString("nachname")
    );
    

    public List<Buerger> findBuergerByPlz(String plz) {
        String sql = "SELECT b.rowid as buergerid, b.EMail, b.Vorname, b.Nachname, b.BerufID, w.rowid as WohnortID, " +
                "br.BerufName AS berufbezeichnung, b.Passwort " +
                "FROM Buerger b " +
                "JOIN Wohnort w ON b.WohnortID = w.WohnortID " +
                "JOIN Beruf br ON b.BerufID = br.BerufID ";

        if (plz != null && !plz.trim().isEmpty()) {
            sql += "WHERE w.PLZ = ?";
            return jdbcTemplate.query(sql, new Object[]{plz}, buergerRowMapper);
        } else {
            return jdbcTemplate.query(sql, buergerRowMapper);
        }
    }

    @Transactional
    public Buerger saveBuerger(Buerger buerger) {
        return transactionTemplate.execute(status -> {
            Integer berufId = getOrInsertBeruf(buerger.getBerufbezeichnung());
            if (berufId == null) {
                throw new RuntimeException("Beruf konnte nicht erstellt oder abgerufen werden.");
            }

            Buerger existingBuerger = findBuergerByEmail(buerger.getEmail());
            if (existingBuerger != null) {
                return existingBuerger;
            }

            try {
                String insertSql = "INSERT INTO Buerger (Email, Passwort, Vorname, Nachname, BerufID, WohnortID) VALUES (?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(insertSql, buerger.getEmail(), buerger.getPasswort(), buerger.getVorname(), buerger.getNachname(), berufId, buerger.getWohnortid());

                Integer buergerId = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
                buerger.setBuergerid(buergerId);
                return buerger;
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                throw new RuntimeException("Email already exists.");
            }
        });
    }

    private Buerger findBuergerByEmail(String email) {
        try {
            String sql = "SELECT * FROM Buerger WHERE Email = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, buergerRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    private Integer getOrInsertBeruf(String berufbezeichnung) {
        try {
            String berufIdSql = "SELECT BerufID FROM Beruf WHERE BerufName = ?";
            return jdbcTemplate.queryForObject(berufIdSql, new Object[]{berufbezeichnung}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            String insertBerufSql = "INSERT INTO Beruf (BerufName) VALUES (?)";
            jdbcTemplate.update(insertBerufSql, berufbezeichnung);
            return jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
        }
    }



    public List<GewerblicherAnbieter> findGewerblicheAnbieterByEmail(String email) {
        String sql = "SELECT ga.ROWID AS anbieterid,b.rowid as buergerid, b.* , br.BerufName AS berufbezeichnung, ga.Gruendungsjahr, w.* " +
                "FROM GewerblicherAnbieter ga " +
                "JOIN Buerger b ON ga.BuergerEmail = b.EMail " +
                "JOIN Beruf br ON b.BerufID = br.BerufID " +
                "JOIN Wohnort w ON b.WohnortID = w.WohnortID ";

        if (email != null && !email.trim().isEmpty()) {
            sql += "WHERE b.Email LIKE ?";
            return jdbcTemplate.query(sql, new Object[]{"%" + email + "%"}, gewerblicherAnbieterRowMapper);
        } else {
            return jdbcTemplate.query(sql, gewerblicherAnbieterRowMapper);
        }
    }
    private RowMapper<GewerblicherAnbieter> gewerblicherAnbieterRowMapper = (rs, rowNum) -> {
        GewerblicherAnbieter anbieter = new GewerblicherAnbieter();
        anbieter.setAnbieterid(rs.getInt("anbieterid"));
        anbieter.setBuergerid(rs.getInt("buergerid"));
        anbieter.setBerufbezeichnung(rs.getString("berufbezeichnung"));
        anbieter.setGruendungsjahr(rs.getInt("gruendungsjahr"));
        anbieter.setEmail(rs.getString("email"));
        anbieter.setPasswort(rs.getString("passwort"));
        anbieter.setVorname(rs.getString("vorname"));
        anbieter.setNachname(rs.getString("nachname"));
        return anbieter;
    };







    @Transactional
    public GewerblicherAnbieter saveGewerblicherAnbieter(GewerblicherAnbieter anbieter, Buerger buerger) {
        try {
            if (buergerExists(buerger.getEmail())) {
                throw new DataIntegrityViolationException("Email already exists.");
            }

            String insertBuerger = "INSERT INTO Buerger (Email, Passwort, Vorname, Nachname, " +
                    "BerufID, WohnortID) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertBuerger, buerger.getEmail(),
                    buerger.getPasswort(), buerger.getVorname(),
                    buerger.getNachname(),
                    getOrInsertBeruf(buerger.getBerufbezeichnung()),
                    buerger.getWohnortid());
            int buergerId = jdbcTemplate.queryForObject(
                    "SELECT last_insert_rowid()", Integer.class);
            int anbieterid = jdbcTemplate.queryForObject(
                    "SELECT last_insert_rowid()", Integer.class);
            String insertAnbieter = "INSERT INTO GewerblicherAnbieter (BuergerEmail, Gruendungsjahr) VALUES (?, ?)";
            jdbcTemplate.update(insertAnbieter, anbieter.getEmail(), anbieter.getGruendungsjahr());

            anbieter.setBuergerid(buergerId);
            anbieter.setAnbieterid(anbieterid);

            return anbieter;
        } catch (DataAccessException e) {
            if (e.contains(SQLException.class) && e.getMostSpecificCause().getMessage().contains("UNIQUE constraint failed")) {
                throw new DataIntegrityViolationException("Email already exists.");
            }
            throw e;
        }
    }










    public List<Wohnort> findWohnorteByStrasse(String strasse) {
        String sql = "SELECT WohnortID, Stadt, PLZ, Strasse, Hausnummer FROM Wohnort";
        if (strasse != null && !strasse.trim().isEmpty()) {
            sql += " WHERE Strasse LIKE ?";
            return jdbcTemplate.query(sql, new Object[]{"%" + strasse + "%"}, wohnortRowMapper);
        } else {
            return jdbcTemplate.query(sql, wohnortRowMapper);
        }
    }

    private RowMapper<Wohnort> wohnortRowMapper = (rs, rowNum) -> {
        Wohnort wohnort = new Wohnort();
        wohnort.setAdresseid(rs.getInt("WohnortID"));
        wohnort.setStadt(rs.getString("Stadt"));
        wohnort.setPlz(rs.getString("PLZ"));
        wohnort.setStrasse(rs.getString("Strasse"));
        wohnort.setHausnummer(rs.getString("Hausnummer"));
        return wohnort;
    };

    @Transactional
    public Wohnort saveWohnort(Wohnort wohnort) {
        return transactionTemplate.execute(status -> {
            try {
                String insertSql = "INSERT INTO Wohnort (Stadt, PLZ, Strasse, Hausnummer) VALUES (?, ?, ?, ?)";
                jdbcTemplate.update(insertSql, wohnort.getStadt(), wohnort.getPlz(), wohnort.getStrasse(), wohnort.getHausnummer());
                int wohnortId = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
                wohnort.setAdresseid(wohnortId);
                return wohnort;
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                throw new RuntimeException("Duplicate entry for Wohnort");
            }
        });
    }

    public List<Spezialisierung> findSpezialisierungenByBezeichnung(String bezeichnung) {
        String sql = "SELECT SpezialisierungID, SpezialGebiet AS Bezeichnung FROM Spezialisierung";
        if (bezeichnung != null && !bezeichnung.trim().isEmpty()) {
            sql += " WHERE SpezialGebiet LIKE ?";
            return jdbcTemplate.query(sql, new Object[]{"%" + bezeichnung + "%"}, (rs, rowNum) ->
                    new Spezialisierung(rs.getInt("SpezialisierungID"), rs.getString("Bezeichnung"))
            );
        } else {
            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    new Spezialisierung(rs.getInt("SpezialisierungID"), rs.getString("Bezeichnung"))
            );
        }
    }



    @Transactional
    public Spezialisierung addSpezialisierung(String bezeichnung) throws DataIntegrityViolationException, DataAccessException {
        if (bezeichnung == null || bezeichnung.trim().isEmpty() || !bezeichnung.matches("^[A-Za-z ]+$")) {
            throw new DataIntegrityViolationException("Bezeichnung darf nur normale Buchstaben und Ziffern enthalten und darf nicht leer sein.");
        }

        try {
            String sql = "INSERT INTO Spezialisierung (SpezialGebiet) VALUES (?)";
            jdbcTemplate.update(sql, bezeichnung);
            int spezialisierungid = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
            return new Spezialisierung(spezialisierungid, bezeichnung);
        } catch (DuplicateKeyException e) {
            throw new DataIntegrityViolationException("Eine Spezialisierung mit dieser Bezeichnung existiert bereits.");
        } catch (DataAccessException e) {
            if (e.contains(SQLException.class) && e.getMostSpecificCause().getMessage().contains("CHECK constraint failed")) {
                throw new DataIntegrityViolationException("Bezeichnung entspricht nicht den Anforderungen.");
            }
            throw e;
        }
    }


    private RowMapper<Event> eventRowMapper = (rs, rowNum) -> {
        return new Event(
                rs.getInt("EventID"),
                rs.getInt("WohnortID"),
                rs.getString("Titel"),
                rs.getString("beschreibung"),
                rs.getString("Startdatum"),
                rs.getString("Enddatum"),
                rs.getDouble("Kosten")
        );
    };



    public List<Event> getEvents(String titel, Boolean mehrtaegig) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT EventID, WohnortID, Titel, EventBeschreibung AS beschreibung, Startdatum, Enddatum, Kosten FROM Event WHERE 1=1");

        if (titel != null && !titel.isEmpty()) {
            sql.append(" AND Titel LIKE ?");
            params.add("%" + titel + "%");
        }

        if (mehrtaegig != null) {
            sql.append(" AND julianday(Enddatum) - julianday(Startdatum) ");
            sql.append(mehrtaegig ? "> 0" : "= 0");
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), eventRowMapper);
    }

    public List<Gruppe> findGruppenByBezeichnung(String bezeichnung) {
        String sql = "SELECT GruppenID as GruppeID, Gruppenbezeichnung as Bezeichnung, Sprache,Sichtbarkeit as Ist_Privat FROM Gruppe";
        List<Object> params = new ArrayList<>();
        if (bezeichnung != null && !bezeichnung.isEmpty()) {
            sql += " WHERE Bezeichnung LIKE ?";
            params.add("%" + bezeichnung + "%");
        }
        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> new Gruppe(
                rs.getInt("GruppeID"),
                rs.getString("Bezeichnung"),
                rs.getString("Sprache"),
                rs.getBoolean("Ist_Privat")
        ));
    }



    private static final Logger logger = LoggerFactory.getLogger(Repository.class);

    public List<Anzeige> getAnzeigen(Integer nachrichtenAnzahl) {
        String sql = "SELECT a.AnzeigeID, b.rowid AS buergerid, a.Titel, a.AnzeigeBeschreibung AS beschreibung, " +
                "(SELECT COUNT(*) FROM Nachricht n WHERE n.AnzeigeID = a.AnzeigeID) AS nachrichten_anzahl " +
                "FROM Anzeige a " +
                "JOIN Buerger b ON a.BuergerEmail = b.Email ";

        List<Object> params = new ArrayList<>();

        if (nachrichtenAnzahl != null) {
            sql += "HAVING nachrichten_anzahl >= ?";
            params.add(nachrichtenAnzahl);
        }

        logger.info("SQL: " + sql);
        logger.info("Parameters: " + params);

        return jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<>(Anzeige.class));
    }

    public List<Nachricht> getNachrichten(Integer anzeigeid) {
        String sql = "SELECT n.AnzeigeID, b.rowid AS buergerid, n.Text " +
                "FROM Nachricht n " +
                "JOIN Buerger b ON n.BuergerEmail = b.Email";
        List<Object> params = new ArrayList<>();

        if (anzeigeid != null) {
            sql += " WHERE n.AnzeigeID = ?";
            params.add(anzeigeid);
        }

        return jdbcTemplate.query(sql, params.toArray(), nachrichtRowMapper);
    }

    private RowMapper<Nachricht> nachrichtRowMapper = new RowMapper<Nachricht>() {
        @Override
        public Nachricht mapRow(ResultSet rs, int rowNum) throws SQLException {
            Nachricht nachricht = new Nachricht();
            nachricht.setAnzeigeid(rs.getInt("AnzeigeID"));
            nachricht.setBuergerid(rs.getString("buergerid"));  // Annahme, dass 'buergerid' die E-Mail ist
            nachricht.setText(rs.getString("Text"));
            // Ignoriere die NachrichtID bewusst
            return nachricht;
        }
    };







    public List<EventMitBewertung> findEventsMitDurchschnittsbewertung(Integer minGrenze) {
        String sql = "SELECT e.EventID, e.Titel, e.EventBeschreibung as Beschreibung, e.Startdatum, e.Enddatum, e.Kosten, " +
                "COALESCE(ROUND(AVG(b.Skala)), 0) AS avg_bewertung " +
                "FROM Event e " +
                "LEFT JOIN Bewertet b ON e.EventID = b.EventID " +
                "GROUP BY e.EventID, e.Titel, e.EventBeschreibung, e.Startdatum, e.Enddatum, e.Kosten ";

        List<Object> params = new ArrayList<>();
        if (minGrenze != null) {
            sql += "HAVING COALESCE(ROUND(AVG(b.Skala)), 0) >= ?";
            params.add(minGrenze);
        }

        return jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<>(EventMitBewertung.class));
    }



    @Transactional
    public Event saveEvent(Event event, String veranstalterEmail) {
    String eventSql = "INSERT INTO Event (Titel, EventBeschreibung, Startdatum, Enddatum, Kosten, WohnortID, VeranstalterEmail) VALUES (?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(eventSql);
        ps.setString(1, event.getTitel());
        ps.setString(2, event.getBeschreibung());
        ps.setString(3, event.getStartdatum());
        ps.setString(4, event.getEnddatum());
        ps.setDouble(5, event.getKosten());
        ps.setInt(6, event.getWohnortid());
        ps.setString(7, veranstalterEmail);
        return ps;
    });

    int lastInsertedId = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
    event.setEventid(lastInsertedId);

    String besuchenSql = "INSERT INTO Besuchen (BuergerEmail, EventID) VALUES (?, ?)";
    jdbcTemplate.update(besuchenSql, veranstalterEmail, lastInsertedId);

    return event;
}



    public boolean checkWohnortAndVeranstalterExists(int wohnortId, String veranstalterEmail) {
        Integer countWohnort = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Wohnort WHERE WohnortID = ?", new Object[]{wohnortId}, Integer.class);
        Integer countVeranstalter = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Buerger WHERE Email = ?", new Object[]{veranstalterEmail}, Integer.class);
        return (countWohnort != null && countWohnort > 0) && (countVeranstalter != null && countVeranstalter > 0);
    }


    @Transactional
    public Gruppe saveGruppe(Gruppe gruppe, String moderatorEmail) {
        String sql = "INSERT INTO Gruppe (Gruppenbezeichnung, Sprache, Sichtbarkeit, ModeratorEmail) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, gruppe.getBezeichnung(), gruppe.getSprache(), gruppe.isIstPrivat(), moderatorEmail);
        int lastInsertedId = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
        gruppe.setGruppeid(lastInsertedId);
        return gruppe;
    }



    public boolean buergerExists(String email) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Buerger WHERE Email = ?", new Object[]{email}, Integer.class);
        return (count != null && count > 0);
    }

    @Transactional
    public Anzeige saveAnzeige(Anzeige anzeige) {
        String sql = "INSERT INTO Anzeige (Titel, AnzeigeBeschreibung, BuergerEmail) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, anzeige.getTitel(), anzeige.getBeschreibung(), anzeige.getBuergerid());
        int lastInsertedId = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
        anzeige.setAnzeigeid(lastInsertedId);
        return anzeige;
    }

    @Transactional
    public Nachricht saveNachricht(Nachricht nachricht) {
        String sql = "INSERT INTO Nachricht (Text, AnzeigeID, BuergerEmail) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, nachricht.getText(), nachricht.getAnzeigeid(), nachricht.getBuergerid());
        int lastInsertedId = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
        nachricht.setNachrichtID(lastInsertedId);
        return nachricht;
    }





    public Anzeige updateAnzeige(int anzeigeid, String titel, String beschreibung) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("UPDATE Anzeige SET ");

        if (titel != null) {
            sql.append("Titel = ?, ");
            params.add(titel);
        }
        if (beschreibung != null) {
            sql.append("AnzeigeBeschreibung = ?, ");
            params.add(beschreibung);
        }

        sql = new StringBuilder(sql.substring(0, sql.length() - 2)); // Entfernen des letzten Kommas
        sql.append(" WHERE AnzeigeID = ?");
        params.add(anzeigeid);

        jdbcTemplate.update(sql.toString(), params.toArray());
        return findAnzeigeById(anzeigeid);
    }


    public boolean isOwner(int anzeigeid, String userEmail) {
        String sql = "SELECT COUNT(*) FROM Anzeige WHERE AnzeigeID = ? AND BuergerEmail = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{anzeigeid, userEmail}, Integer.class);
        return count != null && count > 0;
    }

    public Anzeige findAnzeigeById(int anzeigeid) {
        return jdbcTemplate.queryForObject("SELECT * FROM Anzeige WHERE AnzeigeID = ?",
                new Object[]{anzeigeid}, new BeanPropertyRowMapper<>(Anzeige.class));
    }

    public int countNachrichtenByAnzeigeId(int anzeigeid) {
        String sql = "SELECT COUNT(*) FROM Nachricht WHERE AnzeigeID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{anzeigeid}, Integer.class);
    }

    public void deleteAnzeigeById(int anzeigeid) {
        String sql = "DELETE FROM Anzeige WHERE AnzeigeID = ?";
        jdbcTemplate.update(sql, anzeigeid);
    }

    public boolean anzeigeExists(int anzeigeid) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Anzeige WHERE AnzeigeID = ?", new Object[]{anzeigeid}, Integer.class);
        return (count != null && count > 0);
    }



    public boolean nachrichtExists(int anzeigeid, int nachrichtid) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Nachricht WHERE AnzeigeID = ? AND NachrichtID = ?",
                new Object[]{anzeigeid, nachrichtid},
                Integer.class);
        return (count != null && count > 0);
    }

    public boolean isAuthorOfNachricht(int nachrichtid, String userEmail) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Nachricht WHERE NachrichtID = ? AND BuergerEmail = ?",
                new Object[]{nachrichtid, userEmail},
                Integer.class);
        return (count != null && count > 0);
    }

    public void deleteNachrichtById(int nachrichtid) {
        jdbcTemplate.update("DELETE FROM Nachricht WHERE NachrichtID = ?", nachrichtid);
    }





