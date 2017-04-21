package com.vdzon.administratie.model

import com.vdzon.administratie.model.boekingen.Boeking
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import java.util.*

@Entity("gebruiker")
data class Gebruiker(
        @Id
        val uuid: String,
        val name: String,
        val username: String,
        val password: String,
        val isAdmin: Boolean,
        val administraties: MutableList<Administratie> = ArrayList()) {

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
            administraties!!.remove(administratieToRemove)
        }
    }

    private fun administratieNummerMatchesUuid(uuid: String?, administratie: Administratie): Boolean {
        return uuid == null && administratie.uuid == null || uuid != null && uuid == administratie.uuid
    }

    fun authenticate(password: String): Boolean {
        return password == password
    }

}
