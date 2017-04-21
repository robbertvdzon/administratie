package com.vdzon.administratie.model.boekingen

import org.mongodb.morphia.annotations.Entity

@Entity("boeking")
abstract class Boeking {
    abstract val uuid: String
    abstract val omschrijving: String
}
