package com.vdzon.administratie.mongo

import com.github.mongobee.Mongobee
import com.github.mongobee.exception.MongobeeException
import com.mongodb.MongoClient

class UpdateMongo {

    @Throws(MongobeeException::class)
    fun start(mongoClient: MongoClient, dbName: String) {
        Mongobee(mongoClient)
                .setChangeLogsScanPackage("com.vdzon.administratie.mongo.changelogs")
                .setDbName(dbName)
                .execute()
    }
}
