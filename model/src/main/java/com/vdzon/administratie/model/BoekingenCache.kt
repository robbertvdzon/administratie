package com.vdzon.administratie.model

import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening

import java.util.ArrayList
import java.util.HashMap

import java.util.stream.Collectors.groupingBy

class BoekingenCache {
    private var alleFactuurBoekingen: Map<String, List<BoekingMetFactuur>> = HashMap<String, List<BoekingMetFactuur>>()
    private var alleRekeningBoekingen: Map<String, List<BoekingMetRekening>> = HashMap<String, List<BoekingMetRekening>>()
    private var alleAfschriftBoekingen: Map<String, List<BoekingMetAfschrift>> = HashMap<String, List<BoekingMetAfschrift>>()

    constructor() {
    }

    constructor(boekingen: List<Boeking>) {
        alleAfschriftBoekingen = boekingen
                .filter{ boeking -> boeking is BoekingMetAfschrift }
                .map({ boeking -> boeking as BoekingMetAfschrift })
                .groupBy { boeking -> boeking.afschriftNummer }

        alleFactuurBoekingen = boekingen
                .filter({ boeking -> boeking is BoekingMetFactuur })
                .map({ boeking -> boeking as BoekingMetFactuur })
                .groupBy { boeking -> boeking.factuurNummer }

        alleRekeningBoekingen = boekingen
                .filter({ boeking -> boeking is BoekingMetRekening })
                .map({ boeking -> boeking as BoekingMetRekening })
                .groupBy { boeking -> boeking.rekeningNummer }
    }

    fun getBoekingenVanFactuur(factuurNummer: String): List<BoekingMetFactuur> {
        val boekingMetFactuurs = alleFactuurBoekingen[factuurNummer]
        return boekingMetFactuurs ?: ArrayList<BoekingMetFactuur>()
    }

    fun getBoekingenVanRekening(rekeningNummer: String): List<BoekingMetRekening> {
        val boekingMetRekenings = alleRekeningBoekingen[rekeningNummer]
        return boekingMetRekenings ?: ArrayList<BoekingMetRekening>()
    }

    fun getBoekingenVanAfschrift(afschriftNummer: String): List<BoekingMetAfschrift> {
        val boekingMetAfschrifts = alleAfschriftBoekingen[afschriftNummer]
        return boekingMetAfschrifts ?: ArrayList<BoekingMetAfschrift>()
    }

}
