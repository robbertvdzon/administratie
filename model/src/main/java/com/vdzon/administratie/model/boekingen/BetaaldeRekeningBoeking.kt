package com.vdzon.administratie.model.boekingen

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening
import org.litote.kmongo.MongoId

class BetaaldeRekeningBoeking(
        override val rekeningNummer: String,
        override val afschriftNummer: String,
        @MongoId
        override val uuid: String) : Boeking(), BoekingMetRekening {

    override val omschrijving: String
        get() = "Betaling van een rekening"

}
