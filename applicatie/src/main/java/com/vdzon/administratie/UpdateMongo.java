package com.vdzon.administratie;

import com.mongodb.Mongo;
import org.mongeez.Mongeez;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by robbert on 13/05/2016.
 */
public class UpdateMongo {

    public void start(Mongo mongo, String dbName){
//        MongoClient mongo = MongoClientFactory.maakMongoClient(config, MongoClient.getDefaultCodecRegistry());
        Mongeez mongeez = new Mongeez();
        mongeez.setFile(new ClassPathResource("dbscripts/mongeez.xml"));
        mongeez.setMongo(mongo);
        mongeez.setDbName(dbName);
        mongeez.process();

    }
}
