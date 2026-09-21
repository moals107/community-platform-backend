package de.hhu.cs.dbs.dbwk.project.controller;

import de.hhu.cs.dbs.dbwk.project.entities.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/")
public class RestController {
    private final Service service;

    public RestController(Service service) {
        this.service = service;
    }

    //Buerger Endpunkte
    @GetMapping("/buerger")
    public ResponseEntity<List<Buerger>> getAllBuergerByPlz(@RequestParam(required = false) String plz) {
        List<Buerger> buergerList = service.getBuergerByPlz(plz);
        return ResponseEntity.ok(buergerList);
    }

    @PostMapping("/buerger")
    public ResponseEntity<?> addBuerger(
            @RequestParam String email,
            @RequestParam String passwort,
            @RequestParam String vorname,
            @RequestParam String nachname,
            @RequestParam int wohnortid,
            @RequestParam String berufbezeichnung) {
        Buerger buerger = new Buerger(0, wohnortid, berufbezeichnung, email, passwort, vorname, nachname);
        Buerger createdBuerger = service.addBuerger(buerger);
        if (createdBuerger != null) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdBuerger.getBuergerid())
                    .toUri();
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating the Buerger");
        }
    }


    @GetMapping("/gewerbliche_anbieter")
    public ResponseEntity<List<GewerblicherAnbieter>> getGewerblicheAnbieter(
            @RequestParam(required = false) String email) {
        List<GewerblicherAnbieter> anbieterList = service.getGewerblicheAnbieterByEmail(email);
        return ResponseEntity.ok(anbieterList);
    }




    @PostMapping("/gewerbliche_anbieter")
    public ResponseEntity<?> addGewerblicherAnbieter(
            @RequestParam("email") String email,
            @RequestParam("passwort") String passwort,
            @RequestParam("vorname") String vorname,
            @RequestParam("nachname") String nachname,
            @RequestParam("berufbezeichnung") String berufbezeichnung,
            @RequestParam("wohnortid") int wohnortid,
            @RequestParam("gruendungsjahr") int gruendungsjahr) {
        try {
            Buerger buerger = new Buerger(0, wohnortid, berufbezeichnung, email, passwort, vorname, nachname);
            GewerblicherAnbieter anbieter = new GewerblicherAnbieter(0, wohnortid, berufbezeichnung, gruendungsjahr, email, passwort, vorname, nachname);
            GewerblicherAnbieter savedAnbieter = service.addGewerblicherAnbieter(anbieter, buerger);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedAnbieter.getAnbieterid())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error while processing your request: " + e.getMessage()));
        }
    }



    @GetMapping("/wohnorte")
    public ResponseEntity<List<Wohnort>> getWohnorteByStrasse(@RequestParam(required = false) String strasse) {
        List<Wohnort> wohnorte = service.getWohnorteByStrasse(strasse);
        return ResponseEntity.ok(wohnorte);
    }

    @PostMapping("/wohnorte")
    public ResponseEntity<?> addWohnort(
            @RequestParam String strasse,
            @RequestParam String hausnummer,
            @RequestParam String plz,
            @RequestParam String stadt) {
        Wohnort wohnort = new Wohnort(0, strasse, hausnummer, plz, stadt); // 0 für die ID, da sie auto-increment ist
        Wohnort createdWohnort = service.addWohnort(wohnort);
        if (createdWohnort != null && createdWohnort.getAdresseid() > 0) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdWohnort.getAdresseid())
                    .toUri();
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating the Wohnort");
        }
    }

    @GetMapping("/spezialisierungen")
    public ResponseEntity<List<Spezialisierung>> getSpezialisierungen(@RequestParam(required = false) String bezeichnung) {
        List<Spezialisierung> spezialisierungen = service.findSpezialisierungenByBezeichnung(bezeichnung);
        return ResponseEntity.ok(spezialisierungen);
    }


    @PostMapping("/spezialisierungen")
    public ResponseEntity<?> addSpezialisierung(@RequestParam String bezeichnung) {
        try {
            Spezialisierung spezialisierung = service.addSpezialisierung(bezeichnung);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(spezialisierung.getSpezialisierungid())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Interner Serverfehler: " + e.getMessage() + "\"}");
        }
    }



    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents(
            @RequestParam(required = false) String titel,
            @RequestParam(required = false) Boolean mehrtaegig) {
        List<Event> events = service.findEvents(titel, mehrtaegig);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/gruppen")
    public ResponseEntity<List<Gruppe>> getGruppen(@RequestParam(required = false) String bezeichnung) {
        List<Gruppe> gruppen = service.findGruppenByBezeichnung(bezeichnung);
        return ResponseEntity.ok(gruppen);
    }



    @GetMapping("/anzeigen")
    public ResponseEntity<List<Anzeige>> getAnzeigen(@RequestParam(required = false) Integer nachrichtenAnzahl) {
        List<Anzeige> anzeigen = service.findAnzeigenByNachrichtenAnzahl(nachrichtenAnzahl);
        return ResponseEntity.ok(anzeigen);
    }

    @GetMapping("/anzeigen/nachrichten")
    public ResponseEntity<List<Nachricht>> getNachrichten(
            @RequestParam(required = false) Integer anzeigeid) {
        List<Nachricht> nachrichten = service.findNachrichten(anzeigeid);
        return ResponseEntity.ok(nachrichten);
    }


    @GetMapping("/events/bewertungen")
    public ResponseEntity<List<EventMitBewertung>> getEventsMitBewertungen(
            @RequestParam(required = false) Integer min_grenze) {
        List<EventMitBewertung> events = service.findEventsMitDurchschnittsbewertung(min_grenze);
        return ResponseEntity.ok(events);
    }


    @PostMapping(value = "/events", consumes = "multipart/form-data")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> addEvent(
            @RequestParam("titel") String titel,
            @RequestParam("beschreibung") String beschreibung,
            @RequestParam("startdatum") String startdatum,
            @RequestParam(value = "enddatum", required = false) String enddatum,
            @RequestParam("kosten") Double kosten,
            @RequestParam("wohnortid") Integer wohnortid,
            Authentication authentication) {
        try {
            String email = authentication.getName();  // E-Mail des angemeldeten Benutzers
            Event event = new Event(0, wohnortid, titel, beschreibung, startdatum, enddatum, kosten);
            Event savedEvent = service.addEvent(event, email);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(savedEvent.getEventid()).toUri();
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error while processing your request: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/gruppen", consumes = "multipart/form-data")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> addGruppe(
            @RequestParam("bezeichnung") String bezeichnung,
            @RequestParam("sprache") String sprache,
            @RequestParam("ist_privat") Boolean istPrivat,
            Authentication authentication) {
        try {
            String moderatorEmail = authentication.getName();
            Gruppe gruppe = new Gruppe(0, bezeichnung, sprache, istPrivat); // GruppenID wird in der Datenbank gesetzt
            Gruppe gespeicherteGruppe = service.addGruppe(gruppe, moderatorEmail);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(gespeicherteGruppe.getGruppeid()).toUri();
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error while processing your request: " + e.getMessage()));
        }
    }

    @PostMapping("/anzeigen")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> addAnzeige(
            @RequestParam("titel") String titel,
            @RequestParam("beschreibung") String beschreibung,
            Authentication authentication) {
        try {
            String email = authentication.getName(); // E-Mail des angemeldeten Benutzers
            Anzeige anzeige = new Anzeige(0, email, 0, titel, beschreibung);
            Anzeige savedAnzeige = service.addAnzeige(anzeige);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(savedAnzeige.getAnzeigeid()).toUri();
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error while processing your request: " + e.getMessage()));
        }
    }

    @PostMapping("/anzeigen/{anzeigeid}/nachrichten")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> addNachricht(
            @PathVariable("anzeigeid") int anzeigeid,
            @RequestParam("text") String text,
            Authentication authentication) {
        try {
            String email = authentication.getName(); // E-Mail des angemeldeten Benutzers
            Nachricht nachricht = new Nachricht(anzeigeid, email, text);
            Nachricht savedNachricht = service.addNachricht(nachricht);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(savedNachricht.getNachrichtID()).toUri();
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error while processing your request: " + e.getMessage()));
        }
    }

    @PatchMapping("/anzeigen/{anzeigeid}")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> updateAnzeige(
            @PathVariable("anzeigeid") int anzeigeid,
            @RequestParam(value = "titel", required = false) String titel,
            @RequestParam(value = "beschreibung", required = false) String beschreibung,
            Authentication authentication) {

        if (titel == null && beschreibung == null) {
            return ResponseEntity.badRequest().body("{\"message\":\"Titel oder Beschreibung müssen angegeben werden.\"}");
        }

        String userEmail = authentication.getName();
        try {
            Anzeige anzeige = service.updateAnzeige(anzeigeid, userEmail, titel, beschreibung);
            if (anzeige == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"Error while processing your request: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/anzeigen/{anzeigeid}")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> deleteAnzeige(@PathVariable("anzeigeid") int anzeigeid, Authentication authentication) {
        String userEmail = authentication.getName();  // E-Mail des angemeldeten Benutzers
        try {
            if (!service.anzeigeExists(anzeigeid)) {
                return ResponseEntity.notFound().build();  // Anzeige existiert nicht
            }

            boolean isOwner = service.isOwner(anzeigeid, userEmail);
            if (!isOwner) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Nicht berechtigt, diese Anzeige zu löschen.\"}");
            }

            boolean hasMessages = service.anzeigeHasNachrichten(anzeigeid);
            if (hasMessages) {
                return ResponseEntity.badRequest().body("{\"message\":\"Anzeige kann nicht gelöscht werden, da sie zugeordnete Nachrichten hat.\"}");
            }

            service.deleteAnzeige(anzeigeid);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"Error while processing your request: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/anzeigen/{anzeigeid}/nachrichten/{nachrichtid}")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> deleteNachricht(
            @PathVariable("anzeigeid") int anzeigeid,
            @PathVariable("nachrichtid") int nachrichtid,
            Authentication authentication) {
        String userEmail = authentication.getName();  // E-Mail des angemeldeten Benutzers
        try {
            if (!service.nachrichtExists(anzeigeid, nachrichtid)) {
                return ResponseEntity.notFound().build();  // Nachricht existiert nicht oder gehört nicht zur Anzeige
            }

            boolean isAuthor = service.isAuthorOfNachricht(nachrichtid, userEmail);
            if (!isAuthor) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Nicht berechtigt, diese Nachricht zu löschen.\"}");
            }

            service.deleteNachricht(nachrichtid);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"Error while processing your request: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/events/{eventid}/bewertungen")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> addBewertung(
            @PathVariable int eventid,
            @RequestParam int punktzahl,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            // Verwende die Service-Instanz, um die Methode aufzurufen
            service.addEventBewertung(eventid, userEmail, punktzahl);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }


}
