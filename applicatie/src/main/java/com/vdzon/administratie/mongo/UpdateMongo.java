package com.vdzon.administratie.mongo;

import com.mongodb.Mongo;
import org.mongeez.Mongeez;
import org.springframework.core.io.ClassPathResource;

public class UpdateMongo {

    public void start(Mongo mongo, String dbName) {
        Mongeez mongeez = new Mongeez();
        mongeez.setFile(new ClassPathResource("dbscripts/mongeez.xml"));
        mongeez.setMongo(mongo);
        mongeez.setDbName(dbName);
        mongeez.process();

    }
}
