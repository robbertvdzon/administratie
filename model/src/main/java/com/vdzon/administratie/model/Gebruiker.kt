package com.vdzon.administratie.model

import com.vdzon.administratie.model.boekingen.Boeking
import org.litote.kmongo.MongoId
import java.io.Serializable
import java.util.*

data class Gebruiker (
        @MongoId
        var uuid: String? = null,
        var name: String? = null,
        var username: String? = null,
        var password: String? = null,
        var isAdmin: Boolean? = null,
        var administraties: MutableList<Administratie> = ArrayList()) : Serializable {

    constructor():
        this(null,null,null,null, null)



    val defaultAdministratie: Administratie
        get() {
            initDefaultAdministratie()
            return administraties[0]
        }

    fun initDefaultAdministratie() {

        if (administraties.size == 0) {
            val administratieGegevens = AdministratieGegevens(
                    uuid = UUID.randomUUID().toString(),
                    name = "mijn admin"
            )

            administraties.add(
                    Administratie(
                            uuid = UUID.randomUUID().toString(),
                            administratieGegevens = administratieGegevens,
                            bestellingen = ArrayList<Bestelling>(),
                            facturen = ArrayList<Factuur>(),
                            adresboek = ArrayList<Contact>(),
                            rekeningen = ArrayList<Rekening>(),
                            afschriften = ArrayList<Afschrift>(),
                            boekingen = ArrayList<Boeking>(),
                            declaraties = ArrayList<Declaratie>()
                    )
            )
        }
    }

    fun addAdministratie(administratie: Administratie) {
        administraties!!.add(administratie)
    }

    fun removeAdministratie(uuid: String) {
        var administratieToRemove: Administratie? = null
        for (administratie in administraties) {
            if (administratieNummerMatchesUuid(uuid, administratie)) {
                administratieToRemove = administratie
            }
        }
        if (administratieToRemove != null) {
            administraties.remove(administratieToRemove)
        }
    }

    private fun administratieNummerMatchesUuid(uuid: String?, administratie: Administratie): Boolean {
        return uuid == null && administratie.uuid == null || uuid != null && uuid == administratie.uuid
    }

    fun authenticate(password: String): Boolean {
        return this.password == password
    }

}
