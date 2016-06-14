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
        Administratie administratie = Administratie.builder().uuid(getNewUuid()).bestellingen(bestellingen).facturen(facturen).adresboek(adresboek).rekeningen(rekeningen).afschriften(afschriften)
                .declaraties(declaraties).administratieGegevens(new AdministratieGegevens()).build();
        List<Administratie> administraties = new ArrayList<>();
        administraties.add(administratie);
        Gebruiker gebruiker = Gebruiker.builder().uuid(getNewUuid()).name(name).username(username).password(passwd).admin(admin).administraties(administraties).build();
        datastore.save(gebruiker);
    }

    private static String getNewUuid() {
        return UUID.randomUUID().toString();
    }


}
