--Events, die von gewerblichen Anbietern veranstaltet werden und laenger
-- als zwei Tage dauern:

SELECT e.EventID, e.Titel, e.EventBeschreibung, e.Startdatum, e.Enddatum, e.Kosten, e.VeranstalterEmail
FROM Event e
         JOIN GewerblicherAnbieter g ON e.VeranstalterEmail = g.BuergerEmail
WHERE julianday(e.Enddatum) - julianday(e.Startdatum) > 2;

--Buerger, die mit mindestens einem weiteren Buerger zusammenwohnen, wobei beide den gleichen Beruf ausueben:

SELECT DISTINCT b1.EMail, b1.Vorname, b1.Nachname, b1.BerufID
FROM Buerger b1
         JOIN WohntZusammenMit w ON b1.EMail = w.BuergerEmail1 OR b1.EMail = w.BuergerEmail2
         JOIN Buerger b2 ON (b2.EMail = w.BuergerEmail2 OR b2.EMail = w.BuergerEmail1) AND b1.EMail != b2.EMail
WHERE b1.BerufID = b2.BerufID;

--Alle gewerblichen Anbieter, dessen Gruendungsjahr nach 1997 ist:

SELECT BuergerEmail, Gruendungsjahr
FROM GewerblicherAnbieter
WHERE strftime('%Y', Gruendungsjahr) > 1997;
