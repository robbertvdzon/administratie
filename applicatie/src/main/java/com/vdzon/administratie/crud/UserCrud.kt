package com.vdzon.administratie.crud

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.result.UpdateResult
import com.vdzon.administratie.model.*
import com.vdzon.administratie.mongo.Mongo
import org.litote.kmongo.*

import javax.inject.Singleton
import java.util.Random
import java.util.UUID

@Singleton
class UserCrud {

    private val datastore: MongoDatabase

    init {
        datastore = Mongo.mongo.datastore
        println(allGebruikers)
    }

    val allGebruikers: List<Gebruiker>
        get() = datastore.getCollection<Gebruiker>().find().toList()

    fun getGebruiker(uuid: String?): Gebruiker? {
        if (uuid == null) {
            return null
        }
        return this.datastore.getCollection<Gebruiker>().findOneById(uuid)
    }

    fun getGebruikerByUsername(username: String): Gebruiker? {
        return this.datastore.getCollection<Gebruiker>().findOne("{username: '$username'}")

//        val query = this.datastore.createQuery(Gebruiker::class.java)
//        query.field("username").equal(username)
//        val gebruiker = query.get()
//        return gebruiker
    }

    fun addGebruiker(gebruiker: Gebruiker) {
        this.datastore.getCollection<Gebruiker>().insertOne(gebruiker)
//        this.datastore.save(gebruiker)
    }

    fun deleteGebruiker(uuid: String?) {
        if (uuid == null) return
        this.datastore.getCollection<Gebruiker>().deleteOneById(uuid)
//        this.datastore.delete(Gebruiker::class.java, uuid)
    }

    fun updateGebruiker(gebruiker: Gebruiker?) {
        if (gebruiker!=null) this.datastore.getCollection<Gebruiker>().replaceOneById(gebruiker.uuid!!, gebruiker)
    }


}
