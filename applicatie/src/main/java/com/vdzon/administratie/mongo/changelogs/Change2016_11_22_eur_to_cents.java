package com.vdzon.administratie.mongo.changelogs;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.LocalDateTimeConverter;
import org.jongo.Jongo;
import org.jongo.MongoCursor;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class Change2016_11_22_eur_to_cents {

    @ChangeSet(order = "001", id = "euro_to_cents", author = "Robbert")
    public void euroToCents(MongoClient mongoClient, String dbName){
        Datastore datastore = buildDatastore(mongoClient, dbName);
        List<Gebruiker> gebruikers = datastore.createQuery(Gebruiker.class).asList();
        for (Gebruiker gebruiker:gebruikers){
            // update facturen
            for (Factuur factuur:gebruiker.getDefaultAdministratie().getFacturen()){
                Factuur newFactuur = Factuur.newBuilder(factuur).bedragExBtwCent((long)factuur.getBedragExBtw()*100).build();
                gebruiker.getDefaultAdministratie().removeFactuur(newFactuur.getUuid());
                gebruiker.getDefaultAdministratie().addFactuur(newFactuur);
            }
            datastore.delete(Gebruiker.class, gebruiker.getUuid());
            datastore.save(gebruiker);
        }
    }

    private Datastore buildDatastore(MongoClient mongoClient, String dbName) {
        final Morphia morphia = new Morphia();
        morphia.getMapper().getConverters().addConverter(new LocalDateTimeConverter());
        return morphia.createDatastore(mongoClient, dbName);
    }
}
