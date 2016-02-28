package com.vdzon.weegschaal.crud;

import com.mongodb.MongoClient;
import com.vdzon.weegschaal.model.Gewicht;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GewichtenCrud {

    private Datastore datastore;

    public GewichtenCrud() {
        final Morphia morphia = new Morphia();
        System.out.println("connect to mongo");
        morphia.mapPackage("org.mongodb.morphia.example");
        String mongoDbPort = System.getProperties().getProperty("mongoDbPort");
        String mongoDbHostname = System.getProperties().getProperty("mongoDbHost");
        String host = mongoDbHostname == null ? "localhost" : mongoDbHostname;
        System.out.println("host:" + host);
        System.out.println("mongoDbPort:" + mongoDbPort == null ? "default" : mongoDbPort);
        MongoClient mongoClient;
        if (mongoDbPort != null) {
            mongoClient = new MongoClient(host, Integer.parseInt(mongoDbPort));
        } else {
            mongoClient = new MongoClient(host);
        }
        datastore = morphia.createDatastore(mongoClient, "administratie");

        datastore.ensureIndexes();
    }

    public List<Gewicht> getAllGewichten() {
        return this.datastore.createQuery(Gewicht.class).asList();
    }

    public Gewicht getGewicht(String id) {
        return this.datastore.get(Gewicht.class, id);
    }

    public void addGewicht(Gewicht gewicht) {
        this.datastore.save(gewicht);
    }

    public void deleteGewicht(String uuid) {
        this.datastore.delete(Gewicht.class, uuid);
    }

    public void updateGewicht(Gewicht gewicht) {
        this.datastore.save(gewicht);
    }

}
