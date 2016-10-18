package com.vdzon.administratie.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.LocalDateTimeConverter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.List;

public class Mongo {
    private static Mongo mongo = new Mongo();

    private Datastore datastore;

    private Mongo() {
        init();
        createAdminUserWhenNotExists();
    }

    private void createAdminUserWhenNotExists(){
        List<Gebruiker> gebruikers = this.datastore.createQuery(Gebruiker.class).asList();
        if (gebruikers.isEmpty()){
            TestDataGenerator.buildTestData("admin", "admin", "admin", true, datastore);
        }
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
        String mongoDbUsername = System.getProperties().getProperty("mongoDbUsername");
        String mongoDbPasswd = System.getProperties().getProperty("mongoDbPasswd");
        String mongoDbName = System.getProperties().getProperty("mongoDbName");

        System.out.println("Check parameters:");
        System.out.println("mongoDbPort:"+mongoDbPort);
        System.out.println("mongoDbHost:"+mongoDbHostname);
        System.out.println("mongoDbUsername:"+mongoDbUsername);
        System.out.println("mongoDbPasswd:"+mongoDbPasswd);
        System.out.println("mongoDbName:"+mongoDbName);


        System.out.println("usage voorbeeld: -DmongoDbHost=\"37.97.149.78\" -DmongoDbUsername=\"robbert\" -DmongoDbPasswd=\"hdkash746ioye123jhg\" -DmongoDbName=\"mijndb\"");

        String host = mongoDbHostname == null ? "localhost" : mongoDbHostname;
        String port = mongoDbPort == null ? "27017" : mongoDbPort;
        String username = mongoDbUsername == null ? "" : mongoDbUsername;
        String passwd = mongoDbPasswd == null ? "" : mongoDbPasswd;
        String dbName= mongoDbName == null ? "zzp-administratie" : mongoDbName;

        String usernamePasswd = username.length()==0 ? "" : username+":"+passwd+"@";
        String url = "mongodb://"+usernamePasswd+host+":"+port;
        System.out.println("Login using: "+url);

        MongoClientURI uri = new MongoClientURI(url);
        MongoClient mongoClient = new MongoClient(uri);
        datastore = morphia.createDatastore(mongoClient, dbName);
        datastore.ensureIndexes();

//        new UpdateMongo().start(mongoClient, dbName);
    }


}
