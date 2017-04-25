package com.vdzon.administratie.model.boekingen.relaties

interface BoekingMetRekening : BoekingMetAfschrift {
    override val uuid: String
    val rekeningNummer: String
}
