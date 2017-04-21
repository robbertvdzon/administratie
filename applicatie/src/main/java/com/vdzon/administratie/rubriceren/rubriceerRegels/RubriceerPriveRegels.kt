package com.vdzon.administratie.rubriceren.rubriceerRegels

import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.model.boekingen.PriveBetalingBoeking
import com.vdzon.administratie.rubriceren.model.RubriceerAction
import com.vdzon.administratie.rubriceren.model.RubriceerRegel

import java.util.UUID

class RubriceerPriveRegels : RubriceerHelper() {


    @RubriceerRuleCommit
    fun processRegel(regel: RubriceerRegel, gebruiker: Gebruiker) {
        val afschrift = regel.afschrift.toAfschrift()
        when (regel.rubriceerAction) {
            RubriceerAction.PRIVE -> {
                val priveBetalingBoeking = PriveBetalingBoeking(
                        regel.afschrift.nummer,
                        UUID.randomUUID().toString())

                gebruiker.defaultAdministratie.addBoeking(priveBetalingBoeking)
            }
        }
    }


}
