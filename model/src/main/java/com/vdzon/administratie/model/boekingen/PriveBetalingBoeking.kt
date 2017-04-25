package com.vdzon.administratie.model.boekingen

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import org.litote.kmongo.MongoId

class PriveBetalingBoeking(
        override val afschriftNummer: String,
        @MongoId
        override val uuid: String

) : Boeking(), BoekingMetAfschrift {

    override val omschrijving: String
        get() = "Prive betaling"

}
