package com.vdzon.administratie.model.boekingen

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening
import org.mongodb.morphia.annotations.Id

class BetaaldeRekeningBoeking(
        override val rekeningNummer: String,
        override val afschriftNummer: String,
        @Id
        override val uuid: String) : Boeking(), BoekingMetRekening {

    override val omschrijving: String
        get() = "Betaling van een rekening"

}
