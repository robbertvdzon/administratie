package com.vdzon.administratie.mongo;

import com.github.mongobee.Mongobee;
import com.github.mongobee.exception.MongobeeException;
import com.mongodb.MongoClient;

public class UpdateMongo {

    public void start(MongoClient mongoClient, String dbName) throws MongobeeException {
        new Mongobee(mongoClient)
                .setChangeLogsScanPackage("com.vdzon.administratie.mongo.changelogs")
                .setDbName(dbName)
                .execute();
    }
}
