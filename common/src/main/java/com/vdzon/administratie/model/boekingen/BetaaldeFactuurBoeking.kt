package com.vdzon.administratie.model.boekingen

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import org.mongodb.morphia.annotations.Id

class BetaaldeFactuurBoeking (
        override val factuurNummer: String,
        override val afschriftNummer: String,
        @Id
        override val uuid: String) : Boeking(), BoekingMetFactuur {

    override val omschrijving: String
        get() = "Betaalde factuur"

}
