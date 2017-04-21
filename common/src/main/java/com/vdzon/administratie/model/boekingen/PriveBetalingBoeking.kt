package com.vdzon.administratie.model.boekingen

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import org.mongodb.morphia.annotations.Id

class PriveBetalingBoeking(
        override val afschriftNummer: String,
        @Id
        override val uuid: String

) : Boeking(), BoekingMetAfschrift {

    override val omschrijving: String
        get() = "Prive betaling"

}
