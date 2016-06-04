package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity("administratie")
public class Administratie {

    @Id
    private String uuid;
    private AdministratieGegevens administratieGegevens;
    private List<Bestelling> bestellingen = new ArrayList<>();
    private List<Factuur> facturen = new ArrayList<>();
    private List<Contact> adresboek = new ArrayList<>();
    private List<Rekening> rekeningen = new ArrayList<>();
    private List<Afschrift> afschriften = new ArrayList<>();
    private List<Declaratie> declaraties = new ArrayList<>();

    public Administratie() {
    }

    public Administratie(String uuid, List<Bestelling> bestellingen, List<Factuur> facturen, List<Contact> adresboek, List<Rekening> rekeningen, List<Afschrift> afschriften, List<Declaratie> declaraties, AdministratieGegevens administratieGegevens) {
        this.uuid = uuid;
        this.bestellingen = bestellingen;
        this.facturen = facturen;
        this.adresboek = adresboek;
        this.rekeningen = rekeningen;
        this.afschriften = afschriften;
        this.declaraties = declaraties;
        this.administratieGegevens = administratieGegevens;
    }

    public String getUuid() {
        return uuid;
    }

    public AdministratieGegevens getAdministratieGegevens() {
        return administratieGegevens;
    }

    public void setAdministratieGegevens(AdministratieGegevens administratieGegevens) {
        this.administratieGegevens = administratieGegevens;
    }

    public List<Bestelling> getBestellingen() {
        return Collections.unmodifiableList(new ArrayList<>(bestellingen));
    }

    public List<Factuur> getFacturen() {
        return Collections.unmodifiableList(new ArrayList<>(facturen));
    }

    public List<Contact> getAdresboek() {
        return Collections.unmodifiableList(new ArrayList<>(adresboek));
    }

    public List<Rekening> getRekeningen() {
        return Collections.unmodifiableList(new ArrayList<>(rekeningen));
    }

    public List<Afschrift> getAfschriften() {
        return Collections.unmodifiableList(new ArrayList<>(afschriften));
    }

    public List<Declaratie> getDeclaraties() {
        return Collections.unmodifiableList(new ArrayList<>(declaraties));
    }

    public void addFactuur(Factuur factuur) {
        facturen.add(factuur);
    }

    public Factuur getFactuur(String uuid) {
        for (Factuur factuur : getFacturen()) {
            if (factuurUuidMatchesUuid(uuid, factuur)) {
                return factuur;
            }
        }
        return null;
    }

    public Factuur getFactuurByFactuurNummer(String factuurNummer) {
        for (Factuur factuur : getFacturen()) {
            if (factuurNummerMatchesFactuurNummer(factuurNummer, factuur)) {
                return factuur;
            }
        }
        return null;
    }

    public void removeFactuur(String uuid) {
        Factuur factuurToRemove = null;
        for (Factuur factuur : getFacturen()) {
            if (factuurUuidMatchesUuid(uuid, factuur)) {
                factuurToRemove = factuur;
            }
        }
        if (factuurToRemove != null) {
            facturen.remove(factuurToRemove);
        }
    }

    private boolean factuurUuidMatchesUuid(String uuid, Factuur factuur) {
        return uuid == null && factuur.getUuid() == null || uuid != null && uuid.equals(factuur.getUuid());
    }

    private boolean factuurNummerMatchesFactuurNummer(String factuurNummer, Factuur factuur) {
        return factuurNummer == null && factuur.getFactuurNummer() == null || factuurNummer != null && factuurNummer.equals(factuur.getFactuurNummer());
    }

    public void addBestelling(Bestelling bestelling) {
        bestellingen.add(bestelling);
    }

    public Bestelling getBestelling(String uuid) {
        for (Bestelling bestelling : getBestellingen()) {
            if (bestellingNummerMatchesUuid(uuid, bestelling)) {
                return bestelling;
            }
        }
        return null;
    }

    public Bestelling getBestellingByBestellingNummer(String bestellingNummer) {
        for (Bestelling bestelling : getBestellingen()) {
            if (bestellingNummerMatchesBestellingNummer(bestellingNummer, bestelling)) {
                return bestelling;
            }
        }
        return null;
    }



    public void removeBestelling(String uuid) {
        Bestelling bestellingToRemove = null;
        for (Bestelling bestelling : getBestellingen()) {
            if (bestellingNummerMatchesUuid(uuid, bestelling)) {
                bestellingToRemove = bestelling;
            }
        }
        if (bestellingToRemove != null) {
            bestellingen.remove(bestellingToRemove);
        }
    }

    private boolean bestellingNummerMatchesUuid(String uuid, Bestelling bestelling) {
        return uuid == null && bestelling.getUuid() == null || uuid != null && uuid.equals(bestelling.getUuid());
    }

    private boolean bestellingNummerMatchesBestellingNummer(String bestellingNummer, Bestelling bestelling) {
        return bestellingNummer == null && bestelling.getBestellingNummer() == null || bestellingNummer != null && bestellingNummer.equals(bestelling.getBestellingNummer());
    }


    public void addContact(Contact contact) {
        adresboek.add(contact);
    }

    public void removeContact(String uuid) {
        List<Contact> adresboekClone = getAdresboek();
        for (Contact contact : adresboekClone) {
            if (contact.getUuid().equals(uuid)) {
                this.adresboek.remove(contact);
            }
        }
    }

    public void addRekening(Rekening rekening) {
        rekeningen.add(rekening);
    }

    public void removeRekening(String uuid) {
        List<Rekening> rekeningenClone = getRekeningen();
        for (Rekening rekening : rekeningenClone) {
            if (rekening.getUuid().equals(uuid)) {
                this.rekeningen.remove(rekening);
            }
        }
    }

    public void addDeclaratie(Declaratie declaratie) {
        declaraties.add(declaratie);
    }

    public void removeDeclaratie(String uuid) {
        List<Declaratie> declaratiesClone = getDeclaraties();
        for (Declaratie declaratie : declaratiesClone) {
            if (declaratie.getUuid().equals(uuid)) {
                this.declaraties.remove(declaratie);
            }
        }
    }

    public void addAfschrift(Afschrift afschrift) {
        afschriften.add(afschrift);
    }

    public void removeAfschrift(String uuid) {
        List<Afschrift> afschriftenClone = getAfschriften();
        for (Afschrift afschrift : afschriftenClone) {
            if (afschrift.getUuid().equals(uuid)) {
                this.afschriften.remove(afschrift);
            }
        }
    }

}
