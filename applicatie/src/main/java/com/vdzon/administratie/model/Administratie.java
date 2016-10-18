package com.vdzon.administratie.model;

import com.vdzon.administratie.model.boekingen.Boeking;
import lombok.*;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
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
    private List<Boeking> boekingen = new ArrayList<>();

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

    public List<Boeking> getBoekingen() {
        if (boekingen==null) return Collections.unmodifiableList(new ArrayList<>());
        return Collections.unmodifiableList(new ArrayList<>(boekingen));
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

    public Rekening getRekening(String uuid) {
        for (Rekening rekening : getRekeningen()) {
            if (rekeningUuidMatchesUuid(uuid, rekening)) {
                return rekening;
            }
        }
        return null;
    }

    private boolean rekeningUuidMatchesUuid(String uuid, Rekening rekening) {
        return uuid == null && rekening.getUuid() == null || uuid != null && uuid.equals(rekening.getUuid());
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

    public void removeAfschrift(String nummer) {
        List<Afschrift> afschriftenClone = getAfschriften();
        for (Afschrift afschrift : afschriftenClone) {
            if (afschrift.getNummer().equals(nummer)) {
                this.afschriften.remove(afschrift);
            }
        }
    }

    public void addBoeking(Boeking boeking) {
        boekingen.add(boeking);
    }

    public void removeBoeking(String uuid) {
        List<Boeking> boekingenClone = getBoekingen();
        for (Boeking boeking : boekingenClone) {
            if (boeking.getUuid().equals(uuid)) {
                this.boekingen.remove(boeking);
            }
        }
    }

}
