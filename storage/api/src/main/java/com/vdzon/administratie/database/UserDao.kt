package com.vdzon.administratie.database

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.vdzon.administratie.model.Gebruiker

interface UserDao {
    fun getAllGebruikers(): List<Gebruiker>
    fun getGebruiker(uuid: String?): Gebruiker?
    fun getGebruikerByUsername(username: String): Gebruiker?
    fun addGebruiker(gebruiker: Gebruiker)
    fun deleteGebruiker(uuid: String?): DeleteResult?
    fun updateGebruiker(gebruiker: Gebruiker?): UpdateResult?
}
