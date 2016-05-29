package com.vdzon.administratie.mongo;

import com.vdzon.administratie.model.*;
import org.mongodb.morphia.Datastore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestDataGenerator {

    public static void buildTestData(String username, String name, String passwd, boolean admin, Datastore datastore) {
        System.out.println("Maak admin user");
        List<Contact> adresboek = new ArrayList<>();
        List<Bestelling> bestellingen = new ArrayList<>();
        List<Factuur> facturen = new ArrayList<>();
        List<Rekening> rekeningen = new ArrayList<>();
        List<Afschrift> afschriften = new ArrayList<>();
        List<Declaratie> declaraties = new ArrayList<>();
        Administratie administratie = new Administratie(getNewUuid(), bestellingen, facturen, adresboek, rekeningen, afschriften, declaraties, new AdministratieGegevens());
        List<Administratie> administraties = new ArrayList<>();
        administraties.add(administratie);
        Gebruiker gebruiker = new Gebruiker(getNewUuid(), name, username, passwd, admin, administraties);
        datastore.save(gebruiker);
    }

    private static String getNewUuid() {
        return UUID.randomUUID().toString();
    }


}
