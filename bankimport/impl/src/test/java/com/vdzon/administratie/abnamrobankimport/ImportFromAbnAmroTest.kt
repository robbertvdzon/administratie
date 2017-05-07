package com.vdzon.administratie.abnamrobankimport

import com.vdzon.administratie.bankimport.ImportFromBank
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.Gebruiker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths


internal class ImportFromAbnAmroTest {

    @Test
    @DisplayName("should parse file")
    fun testImportWithSampleData() {
        val importFromBank: ImportFromBank = ImportFromAbnAmro()
        val path: Path = Paths.get(ClassLoader.getSystemResource("bankimport.tab").toURI())
        val gebruiker: Gebruiker = Gebruiker()
        val afschriften: List<Afschrift> = importFromBank.parseFile(path, gebruiker)
        assertEquals(15, afschriften.size)
        val trtpRegel = afschriften[0]

        assertEquals("123456789201701232253,30/TRTP/SEPA OVERBOEKING/IBAN/NL42INGB0005734874/BIC/INGBNL2A/NAME/HUMINT/REMI/2016008 resterend bedrag. Excuus./EREF/NOTPROVIDEDHUMINT2016008 resterend bedrag. Excuus.",trtpRegel.uuid)
        assertEquals("1001",trtpRegel.nummer)
        assertEquals("123456789",trtpRegel.rekening)
        assertEquals("2016008 resterend bedrag. Excuus.",trtpRegel.omschrijving)
        assertEquals("HUMINT",trtpRegel.relatienaam)
        assertEquals("2017-01-23",trtpRegel.boekdatum.toString())
        assertEquals("2253.3",trtpRegel.bedrag.toString())

        val sepaRegel = afschriften[1]
        assertEquals("123456789ongeldigeDatum-1820,54/TRTP/SEPA OVERBOEKING/IBAN/NL44ABNA0541739336/BIC/ABNANL2A/NAME/RC VAN DER ZON CJ/REMI/overboeking/EREF/NOTPROVIDEDRC VAN DER ZON CJoverboeking",sepaRegel.uuid)
        assertEquals("1002",sepaRegel.nummer)
        assertEquals("123456789",sepaRegel.rekening)
        assertEquals("overboeking",sepaRegel.omschrijving)
        assertEquals("RC VAN DER ZON CJ",sepaRegel.relatienaam)
        assertEquals("2017-05-07",sepaRegel.boekdatum.toString())
        assertEquals("-1820.54",sepaRegel.bedrag.toString())
    }

    @Test
    @DisplayName("should not duplicate afschriften")
    fun testNoDuplicateAfschriften() {
        val importFromBank: ImportFromBank = ImportFromAbnAmro()
        val path: Path = Paths.get(ClassLoader.getSystemResource("bankimport.tab").toURI())
        val gebruiker: Gebruiker = Gebruiker()
        val afschriftenAfterFirst: List<Afschrift> = importFromBank.parseFile(path, gebruiker)
        assertEquals(15, afschriftenAfterFirst.size)
        gebruiker.defaultAdministratie.afschriften.addAll(afschriftenAfterFirst)
        val afschriftenAfterSecond: List<Afschrift> = importFromBank.parseFile(path, gebruiker)
        assertEquals(0, afschriftenAfterSecond.size)
    }

    @Test
    @DisplayName("should not parse file")
    fun testImportWithNonExistingSampleData() {
        val importFromBank: ImportFromBank = ImportFromAbnAmro()
        val path: Path = Paths.get("noexists.tab")
        val gebruiker: Gebruiker = Gebruiker()
        val afschriften: List<Afschrift> = importFromBank.parseFile(path, gebruiker)
        assertEquals(0, afschriften.size)
    }



}