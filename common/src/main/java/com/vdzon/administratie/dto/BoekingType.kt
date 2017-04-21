package com.vdzon.administratie.dto

enum class BoekingType private constructor(private val type: Int) {
    REKENING(0), FACTUUR(1), PRIVE(2), NONE(3)

}
