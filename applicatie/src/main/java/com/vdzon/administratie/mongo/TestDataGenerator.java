package com.vdzon.administratie.mongo;

import com.vdzon.administratie.model.*;
import org.mongodb.morphia.Datastore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestDataGenerator {

    public static void buildTestData(String username, String name, String passwd, Datastore datastore) {
        System.out.println("Test use robbert al bestaat in database");
        System.out.println("Maak gebruiker robbert aan met testdata");
        List<Contact> adresboek = new ArrayList<>();
        List<Factuur> facturen = new ArrayList<>();
        adresboek.add(new Contact(getNewUuid(), "001", "loxia", "Utrecht", "Weg1", "1234AB", "NL"));
        adresboek.add(new Contact(getNewUuid(), "002", "Nozem", "Haarlem", "Weg2", "2345AB", "NL"));
        FactuurRegel regel1 = new FactuurRegel("ontwikkeling", getRandomUren(), getRandomPrijs(), 21, getNewUuid());
        FactuurRegel regel2 = new FactuurRegel("ontwikkeling", getRandomUren(), getRandomPrijs(), 21, getNewUuid());
        FactuurRegel regel3 = new FactuurRegel("overwerk", getRandomUren(), getRandomPrijs(), 21, getNewUuid());
        List<FactuurRegel> regels1 = new ArrayList<>();
        regels1.add(regel1);
        regels1.add(regel2);
        List<FactuurRegel> regels2 = new ArrayList<>();
        regels2.add(regel3);
        Factuur factuur1 = new Factuur("2016001", LocalDate.now(), new Contact(getNewUuid(), "001", "loxia", "Utrecht", "Weg1", "1234AB", "NL"), false, regels1, getNewUuid());
        Factuur factuur2 = new Factuur("2016002", LocalDate.now(), new Contact(getNewUuid(), "002", "loxia", "Utrecht", "Weg1", "1234AB", "NL"), false, regels2, getNewUuid());
        facturen.add(factuur1);
        facturen.add(factuur2);
        Administratie administratie = new Administratie(getNewUuid(), name, facturen, adresboek);
        List<Administratie> administraties = new ArrayList<>();
        administraties.add(administratie);
        Gebruiker gebruiker = new Gebruiker(getNewUuid(), name, username, passwd, administraties);
        datastore.save(gebruiker);
    }

    private static int getRandomUren() {
        return new Random(System.currentTimeMillis()).nextInt(200);
    }

    private static int getRandomPrijs() {
        return 30 + new Random(System.currentTimeMillis()).nextInt(30);
    }

    private static String getNewUuid() {
        return UUID.randomUUID().toString();
    }


}
