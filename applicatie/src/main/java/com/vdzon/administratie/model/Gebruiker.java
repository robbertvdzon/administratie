package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity("gebruiker")
public class Gebruiker {

    @Id
    private String uuid;
    private String name;
    private String username;
    private String password;
    private List<Factuur> facturen;
    private List<Klant> adresboek;

    public Gebruiker() {
    }

    public Gebruiker(String uuid, String name, String username, String password, List<Factuur> facturen, List<Klant> adresboek) {
        this.uuid = uuid;
        this.name = name;
        this.username = username;
        this.password = password;
        this.facturen = facturen;
        this.adresboek = adresboek;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public List<Factuur> getFacturen() {
        return Collections.unmodifiableList(facturen == null ? new ArrayList<Factuur>() : facturen);
    }

    public List<Klant> getAdresboek() {
        return Collections.unmodifiableList(adresboek == null ? new ArrayList<Klant>() : adresboek);
    }

    public void addFactuur(Factuur factuur){
        facturen.add(factuur);
    }

    public void removeFactuur(String uuid){
        Factuur factuurToRemove = null;
        for (Factuur factuur : getFacturen()){
            if (factuurNummerMatchesUuid(uuid, factuur)){
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

    public void addContact(Klant klant){
        adresboek.add(klant);
    }

    public void removeKlant(String klantNummer){
        for (Klant klant : getAdresboek()){
            if (klant.getKlantNummer().equals(klantNummer)){
                adresboek.remove(klant);
            }
        }
    }

    public boolean authenticate(String password) {
        return getPassword().equals(password);
    }
}
