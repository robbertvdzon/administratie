package com.vdzon.administratie.crud

import com.vdzon.administratie.model.*
import com.vdzon.administratie.mongo.Mongo
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Query

import javax.inject.Singleton
import java.util.Random
import java.util.UUID

@Singleton
class UserCrud {

    private val datastore: Datastore

    init {
        datastore = Mongo.mongo.datastore
    }

    val allGebruikers: List<Gebruiker>
        get() = this.datastore.createQuery(Gebruiker::class.java).asList()

    fun getGebruiker(uuid: String?): Gebruiker? {
        if (uuid == null) {
            return null
        }
        return this.datastore.get(Gebruiker::class.java, uuid)
    }

    fun getGebruikerByUsername(username: String): Gebruiker {
        val query = this.datastore.createQuery(Gebruiker::class.java)
        query.field("username").equal(username)
        val gebruiker = query.get()
        return gebruiker
    }

    fun addGebruiker(gebruiker: Gebruiker) {
        this.datastore.save(gebruiker)
    }

    fun deleteGebruiker(uuid: String?) {
        if (uuid == null) return
        this.datastore.delete(Gebruiker::class.java, uuid)
    }

    fun updateGebruiker(gebruiker: Gebruiker?) {
        if (gebruiker!=null) this.datastore.save(gebruiker)
    }


}
