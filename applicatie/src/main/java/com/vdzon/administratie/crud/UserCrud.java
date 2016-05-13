package com.vdzon.administratie.crud;

import com.mongodb.MongoClient;
import com.vdzon.administratie.UpdateMongo;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.util.LocalDateTimeConverter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Singleton
public class UserCrud {

    private Datastore datastore;

    public UserCrud() {
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

        buildTestData("q","q","q");
        buildTestData("w","w","w");

        new UpdateMongo().start(mongoClient, "zzpadministratie");
    }

    private void buildTestData(String username, String name, String passwd) {
        System.out.println("Test use robbert al bestaat in database");
        System.out.println("Maak gebruiker robbert aan met testdata");
        List<Klant> adresboek = new ArrayList<>();
        List<Factuur> facturen = new ArrayList<>();
        adresboek.add(new Klant(getNewUuid(),"001","loxia","Utrecht","Weg1","1234AB","NL"));
        adresboek.add(new Klant(getNewUuid(),"002","Nozem","Haarlem","Weg2","2345AB","NL"));
        FactuurRegel regel1 = new FactuurRegel("ontwikkeling",getRandomUren(),getRandomPrijs(),21,getNewUuid());
        FactuurRegel regel2 = new FactuurRegel("ontwikkeling",getRandomUren(),getRandomPrijs(),21,getNewUuid());
        FactuurRegel regel3 = new FactuurRegel("overwerk",getRandomUren(),getRandomPrijs(),21,getNewUuid());
        List<FactuurRegel> regels1 = new ArrayList<>();
        regels1.add(regel1);
        regels1.add(regel2);
        List<FactuurRegel> regels2 = new ArrayList<>();
        regels2.add(regel3);
        Factuur factuur1 = new Factuur("2016001",LocalDate.now(),new Klant(getNewUuid(),"001","loxia","Utrecht","Weg1","1234AB","NL"), false,regels1, getNewUuid());
        Factuur factuur2 = new Factuur("2016002",LocalDate.now(),new Klant(getNewUuid(),"002","loxia","Utrecht","Weg1","1234AB","NL"), false,regels2, getNewUuid());
        facturen.add(factuur1);
        facturen.add(factuur2);
        Administratie administratie = new Administratie(getNewUuid(),name,facturen, adresboek);
        List<Administratie> administraties = new ArrayList<>();
        administraties.add(administratie);
        Gebruiker gebruiker = new Gebruiker(getNewUuid(),name,username,passwd,administraties);
        addGebruiker(gebruiker);
    }

    private int getRandomUren(){
        return new Random(System.currentTimeMillis()).nextInt(200);
    }

    private int getRandomPrijs(){
        return 30+new Random(System.currentTimeMillis()).nextInt(30);
    }

    private String getNewUuid(){
        return UUID.randomUUID().toString();
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
