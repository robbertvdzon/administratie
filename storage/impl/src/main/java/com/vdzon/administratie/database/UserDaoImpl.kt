package com.vdzon.administratie.crud

import com.google.inject.Inject
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.database.AdministratieDatabase
import com.vdzon.administratie.database.MongoDatabase
import com.vdzon.administratie.database.UserDao
import org.litote.kmongo.*


class UserDaoImpl : UserDao {

    @Inject
    lateinit internal var database: AdministratieDatabase


    override fun getAllGebruikers(): List<Gebruiker> = getMongoDatabase().getCollection<Gebruiker>().find().toList()
    override fun getGebruiker(uuid: String?): Gebruiker? = if (uuid == null) null else getMongoDatabase().getCollection<Gebruiker>().findOneById(uuid)
    override fun getGebruikerByUsername(username: String): Gebruiker? = getMongoDatabase().getCollection<Gebruiker>().findOne("{username: '$username'}")
    override fun addGebruiker(gebruiker: Gebruiker) = getMongoDatabase().getCollection<Gebruiker>().insertOne(gebruiker)
    override fun deleteGebruiker(uuid: String?) = if (uuid == null) null else getMongoDatabase().getCollection<Gebruiker>().deleteOneById(uuid)
    override fun updateGebruiker(gebruiker: Gebruiker?) = if (gebruiker == null) null else getMongoDatabase().getCollection<Gebruiker>().replaceOneById(gebruiker.uuid!!, gebruiker)

    private fun getMongoDatabase() = (database as MongoDatabase).datastore


}
