package com.vdzon.administratie.model.boekingen

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import org.litote.kmongo.MongoId

class BetaaldeFactuurBoeking (
        override val factuurNummer: String,
        override val afschriftNummer: String,
        @MongoId
        override val uuid: String) : Boeking(), BoekingMetFactuur {

    override val omschrijving: String
        get() = "Betaalde factuur"

}
