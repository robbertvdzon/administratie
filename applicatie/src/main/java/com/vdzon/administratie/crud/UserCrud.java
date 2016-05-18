package com.vdzon.administratie.crud;

import com.vdzon.administratie.model.*;
import com.vdzon.administratie.mongo.Mongo;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import javax.inject.Singleton;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Singleton
public class UserCrud {

    private Datastore datastore;

    public UserCrud() {
        datastore = Mongo.getMongo().getDatastore();
    }

    public List<Gebruiker> getAllGebruikers() {
        return this.datastore.createQuery(Gebruiker.class).asList();
    }

    public Gebruiker getGebruiker(String uuid) {
        if (uuid == null){
            return null;
        }
        return this.datastore.get(Gebruiker.class, uuid);
    }

    public Gebruiker getGebruikerByUsername(String username) {
        Query<Gebruiker> query = this.datastore.createQuery(Gebruiker.class);
        query.field("username").equal(username);
        Gebruiker gebruiker = query.get();
        return gebruiker;
    }

    public void addGebruiker(Gebruiker gebruiker) {
        this.datastore.save(gebruiker);
    }

    public void deleteGebruiker(String uuid) {
        this.datastore.delete(Gebruiker.class, uuid);
    }

    public void updateGebruiker(Gebruiker gebruiker) {
        this.datastore.save(gebruiker);
    }


}
