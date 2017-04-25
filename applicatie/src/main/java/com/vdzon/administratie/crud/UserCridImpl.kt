package com.vdzon.administratie.crud

import com.mongodb.client.MongoDatabase
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.mongo.Mongo
import org.litote.kmongo.*
import javax.inject.Singleton

@Singleton
class UserCrudImpl : UserCrud {

    constructor() {
        datastore = Mongo.mongo.datastore
    }

    private val datastore: MongoDatabase


    override fun getAllGebruikers(): List<Gebruiker> = datastore.getCollection<Gebruiker>().find().toList()
    override fun getGebruiker(uuid: String?): Gebruiker? = if (uuid == null) null else this.datastore.getCollection<Gebruiker>().findOneById(uuid)
    override fun getGebruikerByUsername(username: String): Gebruiker? = this.datastore.getCollection<Gebruiker>().findOne("{username: '$username'}")
    override fun addGebruiker(gebruiker: Gebruiker) = this.datastore.getCollection<Gebruiker>().insertOne(gebruiker)
    override fun deleteGebruiker(uuid: String?) = if (uuid == null) null else this.datastore.getCollection<Gebruiker>().deleteOneById(uuid)
    override fun updateGebruiker(gebruiker: Gebruiker?) = if (gebruiker == null) null else this.datastore.getCollection<Gebruiker>().replaceOneById(gebruiker.uuid!!, gebruiker)


}
