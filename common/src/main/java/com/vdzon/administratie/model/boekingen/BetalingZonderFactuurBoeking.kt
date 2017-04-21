package com.vdzon.administratie.model.boekingen

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import org.mongodb.morphia.annotations.Id

class BetalingZonderFactuurBoeking(
        override val afschriftNummer: String,
        @Id
        override val uuid: String
) : Boeking(), BoekingMetAfschrift {


    override val omschrijving: String
        get() = "Betaling waar geen rekening van is"

}
