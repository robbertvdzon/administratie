package com.vdzon.administratie.bankimport

import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.Gebruiker
import java.nio.file.Path


abstract class ImportFromBank(){
    abstract fun parseFile(out: Path, gebruiker: Gebruiker): List<Afschrift>
}