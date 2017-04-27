package com.vdzon.administratie.database.changelogs

import com.github.mongobee.changeset.ChangeLog
import com.github.mongobee.changeset.ChangeSet
import com.mongodb.*
import com.vdzon.administratie.model.Factuur
import com.vdzon.administratie.model.Gebruiker
//import com.vdzon.administratie.util.LocalDateTimeConverter
//import org.mongodb.morphia.Datastore
//import org.mongodb.morphia.Morphia

@ChangeLog
class Change2016_11_22_eur_to_cents//    @ChangeSet(order = "001", id = "euro_to_cents", author = "Robbert")
//    public void euroToCents(MongoClient mongoClient, String dbName){
//        Datastore datastore = buildDatastore(mongoClient, dbName);
//        List<Gebruiker> gebruikers = datastore.createQuery(Gebruiker.class).asList();
//        for (Gebruiker gebruiker:gebruikers){
//            // update facturen
//            for (Factuur factuur:gebruiker.getDefaultAdministratie().getFacturen()){
//                Factuur newFactuur = Factuur.newBuilder(factuur).bedragExBtwCent((long)factuur.getBedragExBtw()*100).build();
//                gebruiker.getDefaultAdministratie().removeFactuur(newFactuur.getUuid());
//                gebruiker.getDefaultAdministratie().addFactuur(newFactuur);
//            }
//            datastore.delete(Gebruiker.class, gebruiker.getUuid());
//            datastore.save(gebruiker);
//        }
//    }
