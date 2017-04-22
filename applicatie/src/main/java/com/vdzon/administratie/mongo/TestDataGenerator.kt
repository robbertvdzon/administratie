package com.vdzon.administratie.mongo

import com.mongodb.client.MongoDatabase
import com.vdzon.administratie.model.*
import com.vdzon.administratie.model.boekingen.Boeking
import org.litote.kmongo.getCollection
//import org.mongodb.morphia.Datastore

import java.time.LocalDate
import java.util.ArrayList
import java.util.Random
import java.util.UUID

object TestDataGenerator {

    fun buildTestData(username: String, name: String, passwd: String, admin: Boolean, datastore: MongoDatabase) {
        println("Maak admin user")
        val adresboek = ArrayList<Contact>()
        val bestellingen = ArrayList<Bestelling>()
        val facturen = ArrayList<Factuur>()
        val rekeningen = ArrayList<Rekening>()
        val afschriften = ArrayList<Afschrift>()
        val declaraties = ArrayList<Declaratie>()
        val boekingen = ArrayList<Boeking>()
        val administratie = Administratie(
                newUuid,
                AdministratieGegevens(),
                bestellingen,
                facturen,
                adresboek,
                rekeningen,
                afschriften,
                declaraties,
                boekingen)
        val administraties = ArrayList<Administratie>()
        administraties.add(administratie)
        val gebruiker = Gebruiker(newUuid, name, username, passwd, admin, administraties)
        datastore.getCollection<Gebruiker>().insertOne(gebruiker)
    }

    private val newUuid: String
        get() = UUID.randomUUID().toString()


}
