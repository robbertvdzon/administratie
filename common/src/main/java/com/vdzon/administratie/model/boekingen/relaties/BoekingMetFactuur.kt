package com.vdzon.administratie.model.boekingen.relaties

interface BoekingMetFactuur : BoekingMetAfschrift {
    override val uuid: String
    val factuurNummer: String
}
