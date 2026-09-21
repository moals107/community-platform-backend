PRAGMA auto_vacuum = 1;
PRAGMA encoding = "UTF-8";
PRAGMA foreign_keys = 1;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;

-- Tabellen Definitionen
CREATE TABLE Buerger (
 EMail VARCHAR(255)  NOT NULL COLLATE NOCASE CHECK (
     EMail LIKE '%_@_%._%' AND
     LENGTH(EMail) - LENGTH(REPLACE(EMail, '@', '')) = 1 AND
     SUBSTR(LOWER(EMail), 1, INSTR(EMail, '.') - 1) NOT GLOB '*[^@0-9a-z]*' AND
     SUBSTR(LOWER(EMail), INSTR(EMail, '.') + 1) NOT GLOB '*[^a-z]*'
) PRIMARY KEY,
    Vorname VARCHAR(255) NOT NULL CHECK(Vorname != '' AND Vorname NOT GLOB '*[^ -~]*'),
    Nachname VARCHAR(255) NOT NULL CHECK(Nachname != '' AND Nachname NOT GLOB '*[^ -~]*'),
    BerufID INTEGER,
    WohnortID INTEGER,

    Passwort VARCHAR NOT NULL,
     CHECK (LENGTH(Passwort) BETWEEN 4 AND 8),
     CHECK(Passwort GLOB '*[0-9]*[0-9]*' AND Passwort GLOB '*[A-Z]*[A-Z]*'),
     CHECK(Passwort NOT GLOB '*[^ -~]*'),
     CHECK(Passwort NOT GLOB '*[^0-9A-Za-z]*'), -- Ausschluss von Sonderzeichen
     -- Ensures a consonant is followed by exactly two digits
     CHECK(Passwort NOT GLOB '*[BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz][0-9][A-Z]*'),
     CHECK(Passwort NOT GLOB '*[BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz][0-9][a-z]*'),
     CHECK(Passwort NOT GLOB '*[BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz][A-Z]*'),
     CHECK(Passwort NOT GLOB '*[BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz][a-z]*'),
     CHECK(Passwort NOT GLOB '*[BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz][0-9]'),
     CHECK(Passwort NOT GLOB '*[BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz]'),

    FOREIGN KEY (BerufID) REFERENCES Beruf(BerufID) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (WohnortID) REFERENCES Wohnort(WohnortID) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE Faehigkeit (
    FaehigkeitID INTEGER PRIMARY KEY AUTOINCREMENT,
    FaehigkeitName VARCHAR(255) NOT NULL CHECK(FaehigkeitName GLOB '*[A-Za-z]*' AND FaehigkeitName != '' AND FaehigkeitName NOT GLOB '*[^ -~]*')
    );

CREATE TABLE Beruf (
   BerufID INTEGER PRIMARY KEY AUTOINCREMENT,
   BerufName VARCHAR(255) NOT NULL CHECK(BerufName GLOB '*[A-Za-z]*' AND BerufName != '' AND BerufName NOT GLOB '*[^ -~]*')
    );

CREATE TABLE Wohnort (
    WohnortID INTEGER PRIMARY KEY AUTOINCREMENT,
    Stadt VARCHAR(255) NOT NULL CHECK(Stadt != '' AND Stadt NOT GLOB '*[^ -~]*'),
    PLZ VARCHAR(5) NOT NULL CHECK(PLZ GLOB '[0-9]*' AND LENGTH(PLZ) = 5 AND PLZ NOT GLOB '*[^ -~]*'),
    Strasse VARCHAR(255) NOT NULL CHECK(Strasse != '' AND Strasse NOT GLOB '*[^ -~]*'),
    Hausnummer VARCHAR(255) NOT NULL CHECK(Hausnummer != '' AND Hausnummer NOT GLOB '*[^ -~]*'),
    UNIQUE(Stadt, PLZ, Strasse, Hausnummer)
);

CREATE TABLE Anzeige (
    AnzeigeID INTEGER PRIMARY KEY AUTOINCREMENT,
    Titel VARCHAR(255) NOT NULL CHECK(Titel != '' AND Titel NOT GLOB '*[^ -~]*'),
    AnzeigeBeschreibung VARCHAR(255) NOT NULL CHECK(AnzeigeBeschreibung != '' AND AnzeigeBeschreibung NOT GLOB '*[^ -~]*'),
    BuergerEmail VARCHAR(255) NOT NULL COLLATE NOCASE,
    FOREIGN KEY (BuergerEmail) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Nachricht (
    NachrichtID INTEGER PRIMARY KEY AUTOINCREMENT,
    Text VARCHAR(255) NOT NULL CHECK(Text != '' AND Text NOT GLOB '*[^ -~]*'),
    AnzeigeID INTEGER NOT NULL,
    BuergerEmail VARCHAR(255) NOT NULL COLLATE NOCASE,
    FOREIGN KEY (AnzeigeID) REFERENCES Anzeige(AnzeigeID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (BuergerEmail) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Gruppe (
    GruppenID INTEGER PRIMARY KEY AUTOINCREMENT,
    Gruppenbezeichnung VARCHAR(255) NOT NULL CHECK(Gruppenbezeichnung != '' AND Gruppenbezeichnung NOT GLOB '*[^ -~]*'),
    Sichtbarkeit BOOLEAN NOT NULL CHECK (Sichtbarkeit IN (true, false)),
    Sprache VARCHAR(255) NOT NULL CHECK(Sprache != '' AND Sprache NOT GLOB '*[^ -~]*'),
    ModeratorEmail VARCHAR(255) NOT NULL COLLATE NOCASE,
    FOREIGN KEY (ModeratorEmail) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE GewerblicherAnbieter (
  BuergerEmail VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY,
  Gruendungsjahr INTEGER NOT NULL CHECK (Gruendungsjahr >= 1 ),
  FOREIGN KEY (BuergerEmail) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Event (
   EventID INTEGER PRIMARY KEY AUTOINCREMENT,
   Titel VARCHAR(255) NOT NULL CHECK(Titel != '' AND Titel NOT GLOB '*[^ -~]*'),
    EventBeschreibung VARCHAR(255) NOT NULL CHECK(EventBeschreibung != '' AND EventBeschreibung NOT GLOB '*[^ -~]*'),
    Startdatum DATE NOT NULL
        CHECK(Startdatum IS strftime('%Y-%m-%d', Startdatum)),
    Enddatum DATE
        CHECK(Enddatum IS strftime('%Y-%m-%d', Enddatum)),
    Kosten FLOAT CHECK(Kosten >= 0 AND Kosten = round(Kosten, 2)),
    VeranstalterEmail VARCHAR(255) NOT NULL COLLATE NOCASE,
    WohnortID INTEGER NOT NULL,
    FOREIGN KEY (VeranstalterEmail) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (WohnortID) REFERENCES Wohnort(WohnortID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Spezialisierung (
     SpezialisierungID INTEGER PRIMARY KEY AUTOINCREMENT,
     SpezialGebiet VARCHAR(255) NOT NULL CHECK(SpezialGebiet GLOB '*[A-Za-z]*' AND SpezialGebiet != '')
);


-- Beziehungs-Tabellen (Relationen für Beziehungen mit Fremdschlüsseln)
CREATE TABLE Besitzen (
  BuergerEmail VARCHAR(255) NOT NULL COLLATE NOCASE,
  FaehigkeitID INTEGER NOT NULL,
  FOREIGN KEY (BuergerEmail) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (FaehigkeitID) REFERENCES Faehigkeit(FaehigkeitID) ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY (BuergerEmail, FaehigkeitID)
);

CREATE TABLE Partizipieren (
   BuergerEmail VARCHAR(255) NOT NULL COLLATE NOCASE,
   GruppenID INTEGER NOT NULL,
   FOREIGN KEY (BuergerEmail) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (GruppenID) REFERENCES Gruppe(GruppenID) ON UPDATE CASCADE ON DELETE CASCADE,
   PRIMARY KEY (BuergerEmail, GruppenID)
);

CREATE TABLE HatSpezial (
    GewerblicherAnbieterEmail VARCHAR(255) NOT NULL COLLATE NOCASE,
    SpezialisierungID INTEGER NOT NULL,
    FOREIGN KEY (GewerblicherAnbieterEmail) REFERENCES GewerblicherAnbieter(BuergerEmail) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (SpezialisierungID) REFERENCES Spezialisierung(SpezialisierungID) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (GewerblicherAnbieterEmail, SpezialisierungID)
);

CREATE TABLE Besuchen (
  BuergerEmail VARCHAR(255) NOT NULL COLLATE NOCASE,
  EventID INTEGER NOT NULL,
  FOREIGN KEY (BuergerEmail) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (EventID) REFERENCES Event(EventID) ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY (BuergerEmail, EventID)
);

CREATE TABLE Bewertet (
  BuergerEmail VARCHAR(255) NOT NULL COLLATE NOCASE,
  EventID INTEGER NOT NULL,
  Skala INTEGER CHECK (Skala BETWEEN 1 AND 5),
  FOREIGN KEY (BuergerEmail) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (EventID) REFERENCES Event(EventID) ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY (BuergerEmail, EventID)
);

CREATE TABLE WohntZusammenMit (
  BuergerEmail1 VARCHAR(255) NOT NULL COLLATE NOCASE,
  BuergerEmail2 VARCHAR(255) NOT NULL COLLATE NOCASE,
  FOREIGN KEY (BuergerEmail1) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (BuergerEmail2) REFERENCES Buerger(EMail) ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY (BuergerEmail1, BuergerEmail2),
  CHECK (BuergerEmail1 != BuergerEmail2)
    );

-- Trigger
-- Ein Bürger kann nur Events bewerten, an denen er auch teilgenommen hat.
CREATE TRIGGER Trigger_EventBewertung
    BEFORE INSERT ON Bewertet
    FOR EACH ROW
    WHEN NOT EXISTS (SELECT 1 FROM Besuchen WHERE EventID = NEW.EventID AND BuergerEmail = NEW.BuergerEmail)
BEGIN
    SELECT RAISE(FAIL, 'Ein Buerger kann nur Events bewerten, an denen er teilgenommen hat.');
END;


-- Ein Bürger kann höchstens Mitglied von zwei privaten Gruppen sein.
CREATE TRIGGER Trigger_MaxPrivateGruppen
    BEFORE INSERT ON Partizipieren
    FOR EACH ROW
    WHEN (SELECT COUNT(*) FROM Partizipieren AS p JOIN Gruppe AS g ON p.GruppenID = g.GruppenID
                          WHERE p.BuergerEmail = NEW.BuergerEmail AND g.Sichtbarkeit = 0) >= 2
BEGIN
SELECT RAISE(FAIL, 'Ein Buerger kann nur Mitglied von maximal zwei privaten Gruppen sein.');
END;

-- Annoncen mit zugeordneten Nachrichten können nicht gelöscht werden.
CREATE TRIGGER Trigger_AnzeigeLoeschen
    BEFORE DELETE ON Anzeige
    FOR EACH ROW
    WHEN EXISTS (SELECT 1 FROM Nachricht WHERE AnzeigeID = OLD.AnzeigeID)
BEGIN
    SELECT RAISE(FAIL, 'Annoncen mit zugeordneten Nachrichten können nicht gelöscht werden.');
END;




CREATE TRIGGER Trigger_MaxGruppenmitglieder
    BEFORE INSERT ON Partizipieren
    FOR EACH ROW
    WHEN (SELECT COUNT(*) FROM Partizipieren WHERE GruppenID = NEW.GruppenID) >= 20
BEGIN
SELECT RAISE(FAIL, 'Eine Gruppe kann maximal 20 Buerger enthalten.');
END;

--CREATE TRIGGER Trigger_MinSpezialisierung
 --   AFTER INSERT ON GewerblicherAnbieter
 --   FOR EACH ROW
--    WHEN (SELECT COUNT(*) FROM HatSpezial WHERE GewerblicherAnbieterEmail = NEW.BuergerEmail) < 1
--BEGIN
--SELECT RAISE(FAIL, 'Ein GewerblicherAnbieter muss mindestens eine Spezialisierung haben.');
--END;
