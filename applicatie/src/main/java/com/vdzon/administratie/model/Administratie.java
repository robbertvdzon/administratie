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
    private List<Factuur> facturen = new ArrayList<>();
    private List<Contact> adresboek = new ArrayList<>();

    public Administratie() {
    }

    public Administratie(String uuid, List<Factuur> facturen, List<Contact> adresboek, AdministratieGegevens administratieGegevens) {
        this.uuid = uuid;
        this.facturen = facturen;
        this.adresboek = adresboek;
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

    public List<Factuur> getFacturen() {
        return Collections.unmodifiableList(new ArrayList<>(facturen));
    }

    public List<Contact> getAdresboek() {
        return Collections.unmodifiableList(new ArrayList<>(adresboek));
    }

    public void addFactuur(Factuur factuur) {
        facturen.add(factuur);
    }

    public Factuur getFactuur(String uuid) {
        for (Factuur factuur : getFacturen()) {
            if (factuurNummerMatchesUuid(uuid, factuur)) {
                return factuur;
            }
        }
        return null;
    }

    public void removeFactuur(String uuid) {
        Factuur factuurToRemove = null;
        for (Factuur factuur : getFacturen()) {
            if (factuurNummerMatchesUuid(uuid, factuur)) {
                factuurToRemove = factuur;
            }
        }
        if (factuurToRemove != null) {
            facturen.remove(factuurToRemove);
        }
    }

    private boolean factuurNummerMatchesUuid(String uuid, Factuur factuur) {
        return uuid == null && factuur.getUuid() == null || uuid != null && uuid.equals(factuur.getUuid());
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

}
