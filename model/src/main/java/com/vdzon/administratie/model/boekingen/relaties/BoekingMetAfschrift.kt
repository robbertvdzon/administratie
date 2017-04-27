package com.vdzon.administratie.model.boekingen.relaties

import java.io.Serializable

interface BoekingMetAfschrift : Serializable {
    val uuid: String
    val afschriftNummer: String
}
