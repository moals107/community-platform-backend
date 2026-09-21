-- Beruf Einfügungen
INSERT INTO Beruf (BerufID,BerufName) VALUES
(1,'Softwareentwickler'),
(2,'Grafikdesigner'),
(3,'Netzwerkadministrator');

-- Wohnort Einfügungen
INSERT INTO Wohnort (WohnortID,Stadt, PLZ, Strasse, Hausnummer) VALUES
(1,'Berlin', '10115', 'Hauptstrasse', '1'),
(2,'Muenchen', '80331', 'Nebenweg', '2'),
(3,'Koeln', '50667', 'Am Rhein', '3');

-- Buerger Einfügungen
INSERT INTO Buerger (EMail, Vorname, Nachname, Passwort, BerufID, WohnortID) VALUES
('max@example.com', 'Max', 'Mustermann', 'A1E4', 1, 1),
('bruder@example.com', 'MaxBruder', 'MustermannBruder', 'Ab12D34', 1, 1),
('julia@example.com', 'Julia', 'Schmidt', 'Z12Y34', 2, 2),
('sis@example.com', 'Sis', 'SchmidtSis', 'Z12Y34', 1, 2),
('frank@example.com', 'Frank', 'Weber', 'K12H34', 3, 3),
('chris@example.com', 'Chris', 'WeberChris', 'K12H34', 3, 3);



-- Faehigkeit Einfügungen
INSERT INTO Faehigkeit (FaehigkeitName) VALUES
('Programmieren'),
('Grafikdesign'),
('Netzwerkadministration');


-- Anzeige Einfügungen
INSERT INTO Anzeige (Titel, AnzeigeBeschreibung, BuergerEmail) VALUES
('Suche Mitfahrgelegenheit', 'Ich suche eine Mitfahrgelegenheit nach Muenchen am naechsten Wochenende', 'max@example.com'),
('Biete Grafikdesign', 'Ich biete professionelles Grafikdesign fuer Web und Print', 'julia@example.com'),
('Netzwerkprobleme loesen', 'Ich biete Hilfe bei Netzwerkproblemen und Einrichtung', 'frank@example.com');

-- Nachricht Einfügungen
INSERT INTO Nachricht (Text, AnzeigeID, BuergerEmail) VALUES
('Ich bin interessiert. Koennen wir Details besprechen?', 1, 'julia@example.com'),
('Koennen Sie mir Beispiele Ihrer Arbeit zeigen?', 2, 'frank@example.com'),
('Brauche Hilfe bei einem Netzwerk zu Hause.', 3, 'max@example.com');

-- Gruppe Einfügungen
INSERT INTO Gruppe (Gruppenbezeichnung, Sichtbarkeit, Sprache, ModeratorEmail) VALUES
('IT-Profis', 0, 'Deutsch', 'frank@example.com'),
('Design Liebhaber', 0, 'Englisch', 'julia@example.com'),
('Reisefreunde', 0, 'Deutsch', 'max@example.com');

-- GewerblicherAnbieter Einfügungen
INSERT INTO GewerblicherAnbieter (BuergerEmail, Gruendungsjahr) VALUES
('max@example.com', 2010),
('julia@example.com', 2012),
('frank@example.com', 2008);

-- Event Einfügungen
INSERT INTO Event (Titel, EventBeschreibung, Startdatum, Enddatum, Kosten, VeranstalterEmail, WohnortID) VALUES
('Tech Konferenz', 'Jaehrliche Tech Konferenz mit vielen interessanten Vortraegen.', '2023-11-15', '2023-11-17', 99.99, 'max@example.com', 1),
('Design Workshop', 'Workshop zu den neuesten Trends im Grafikdesign.', '2023-10-05', '2023-10-05', 49.99, 'julia@example.com', 2),
('Netzwerk Seminar', 'Seminar zur Sicherheit in Netzwerken und besten Praktiken.', '2023-12-01', '2023-12-05', 199.99, 'frank@example.com', 3);

-- Spezialisierung Einfügungen
INSERT INTO Spezialisierung (SpezialGebiet) VALUES
('Webentwicklung'),
('UI/UX Design'),
('Cybersicherheit');

-- Besitzen Beziehungen
INSERT INTO Besitzen (BuergerEmail, FaehigkeitID) VALUES
('max@example.com', 1),
('julia@example.com', 2),
('frank@example.com', 3);

-- Partizipieren Beziehungen
INSERT INTO Partizipieren (BuergerEmail, GruppenID) VALUES
('max@example.com', 3),
('max@example.com', 2),
('julia@example.com', 2),
('frank@example.com', 1);

-- HatSpezial Beziehungen
INSERT INTO HatSpezial (GewerblicherAnbieterEmail, SpezialisierungID) VALUES
('max@example.com', 1),
('julia@example.com', 2),
('frank@example.com', 3);

-- Besuchen Beziehungen
INSERT INTO Besuchen (BuergerEmail, EventID) VALUES
('max@example.com', 1),
('julia@example.com', 2),
('frank@example.com', 3);

-- Bewertet Beziehungen
INSERT INTO Bewertet (BuergerEmail, EventID, Skala) VALUES
('max@example.com', 1, 5),
('julia@example.com', 2, 4),
('frank@example.com', 3, 5);

-- WohntZusammenMit Beziehungen
INSERT INTO WohntZusammenMit (BuergerEmail1, BuergerEmail2) VALUES
('max@example.com', 'bruder@example.com'),
('julia@example.com', 'sis@example.com'),
('frank@example.com', 'chris@example.com');
