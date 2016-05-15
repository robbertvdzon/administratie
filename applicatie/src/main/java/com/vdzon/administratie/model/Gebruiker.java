package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity("gebruiker")
public class Gebruiker {

    @Id
    private String uuid;
    private String name;
    private String username;
    private String password;
    private List<Administratie> administraties = new ArrayList<>();

    public Gebruiker() {
    }

    public Gebruiker(String uuid, String name, String username, String password, List<Administratie> administraties) {
        this.uuid = uuid;
        this.name = name;
        this.username = username;
        this.password = password;
        this.administraties = administraties;
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

    public List<Administratie> getAdministraties() {
        return Collections.unmodifiableList(new ArrayList<>(administraties));
    }

    public Administratie getDefaultAdministratie(){
        if (administraties==null){
            administraties = new ArrayList<>();
        }
        if (administraties.size()==0){
            administraties.add(new Administratie(getUuid(),"mijn admin",null,null));
        }
        return administraties.get(0);
    }

    public void addAdministratie(Administratie administratie){
        administraties.add(administratie);
    }

    public void removeAdministratie(String uuid){
        Administratie administratieToRemove = null;
        for (Administratie administratie : getAdministraties()){
            if (administratieNummerMatchesUuid(uuid, administratie)){
                administratieToRemove = administratie;
            }
        }
        if (administratieToRemove != null) {
            administraties.remove(administratieToRemove);
        }
    }

    private boolean administratieNummerMatchesUuid(String uuid, Administratie administratie) {
        return uuid == null && administratie.getUuid() == null || uuid != null && uuid.equals(administratie.getUuid());
    }

    public boolean authenticate(String password) {
        return getPassword().equals(password);
    }

    // TODO: naar utils
    private String getNewUuid() {
        return UUID.randomUUID().toString();
    }

}
