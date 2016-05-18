package com.vdzon.administratie.mongo;

import com.mongodb.MongoClient;
import com.vdzon.administratie.util.LocalDateTimeConverter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class Mongo {
    private static Mongo mongo = new Mongo();

    private Datastore datastore;

    private Mongo() {
        init();
//        TestDataGenerator.buildTestData("q", "q", "q", datastore);
    }

    public static Mongo getMongo() {
        return mongo;
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public void init() {
        final Morphia morphia = new Morphia();
        morphia.getMapper().getConverters().addConverter(new LocalDateTimeConverter());
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
        datastore = morphia.createDatastore(mongoClient, "zzpadministratie");
        datastore.ensureIndexes();

        new UpdateMongo().start(mongoClient, "zzpadministratie");
    }


}
