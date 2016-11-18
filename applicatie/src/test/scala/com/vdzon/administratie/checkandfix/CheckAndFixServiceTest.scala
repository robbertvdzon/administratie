package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.model.{CheckType, FixAction}
import com.vdzon.administratie.model.boekingen.{BetaaldeFactuurBoeking, BetaaldeRekeningBoeking, Boeking}
import com.vdzon.administratie.model._
import org.junit.Assert._
import org.junit.Test

import scala.collection.JavaConversions._
import scala.collection.immutable.Nil


class CheckAndFixServiceTest {
  @Test
  def when_alle_facturen_betaald_then_geen_checkregels(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100), buildAfschrift("a2", 101)))
      .facturen(List(buildFactuur("f1", 100, 0), buildFactuur("f2", 100, 0)))
      .rekeningen(List[Rekening]())
      .boekingen(List(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2"))).build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie)

    assertTrue("", regels.size == 1)
  }

  @Test
  def when_factuur_niet_volledig_geboekt_then_return_checkError(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100), buildAfschrift("a2", 100), buildAfschrift("a3", -100)))
      .facturen(List(buildFactuur("f1", 100, 0), buildFactuur("f2", 100, 0)))
      .rekeningen(List(buildRekening("r1", 100, 80, 20)))
      .boekingen(List(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2"), rekeningBoeking("b3", "r1", "a3"))).build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie)
    regels.stream().forEach(f => println(f.omschrijving))
    assertTrue("", regels.size == 0)
  }


  @Test
  def when_boeking_heeft_nonexisting_factuur_then_return_checkError(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100), buildAfschrift("a2", 100)))
      .facturen(List(buildFactuur("f1", 100, 0)))
      .rekeningen(List[Rekening]())
      .boekingen(List(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2"))).build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie)
    assertTrue("", regels.size == 1)
    assertTrue("", regels(0).rubriceerAction == FixAction.REMOVE_BOEKING)
    assertTrue("", regels(0).checkType == CheckType.FIX_NEEDED)
    assertTrue("", regels(0).boekingUuid.equals("b2"))
  }

  @Test
  def when_boeking_heeft_nonexisting_factuur_then_boeking_removed_after_fix(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100), buildAfschrift("a2", 100)))
      .facturen(List(buildFactuur("f1", 100, 0)))
      .rekeningen(List[Rekening]())
      .boekingen(List(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2"))).build()

    val fixedAdministratie = FixServiceScala.getFixedAdministratie(administratie);
    assertTrue("", fixedAdministratie.getBoekingen.size() == 1)
    assertTrue("", fixedAdministratie.getBoekingen()(0).getUuid.equals("b1"))
  }

  @Test
  def when_boeking_heeft_nonexisting_rekening_then_return_checkError(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100)))
      .facturen(List[Factuur]())
      .rekeningen(List[Rekening]())
      .boekingen(List(rekeningBoeking("b1", "r1", "a1"))).build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie);
    regels.stream().forEach(f => println(f.omschrijving));
    assertTrue("", regels.size == 1)
    assertTrue("", regels(0).rubriceerAction == FixAction.REMOVE_BOEKING)
    assertTrue("", regels(0).checkType == CheckType.FIX_NEEDED)
    assertTrue("", regels(0).boekingUuid.equals("b1"))
  }

  @Test
  def when_boeking_heeft_nonexisting_afschrift_then_return_checkError(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100)))
      .facturen(List(buildFactuur("f1", 100, 0), buildFactuur("f2", 0, 0)))
      .rekeningen(List[Rekening]())
      .boekingen(List(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2"))).build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie);
    regels.stream().forEach(f => println(f.omschrijving));
    assertTrue("", regels.size == 1)
    assertTrue("", regels(0).rubriceerAction == FixAction.REMOVE_BOEKING)
    assertTrue("", regels(0).checkType == CheckType.FIX_NEEDED)
    assertTrue("", regels(0).boekingUuid.equals("b2"))
  }

  @Test
  def when_rekening_niet_volledig_betaald_then_return_checkError(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100)))
      .facturen(List[Factuur]())
      .rekeningen(List(buildRekening("r1", 100, 80, 20)))
      .boekingen(List[Boeking]())
      .build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie);
    regels.stream().forEach(f => println(f.omschrijving));
    assertTrue("", regels.size == 1)
    assertTrue("", regels(0).rubriceerAction == FixAction.NONE)
    assertTrue("", regels(0).checkType == CheckType.WARNING)
  }

  @Test
  def when_one_rekening_then_return_geen_errors(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List[Afschrift]())
      .facturen(List[Factuur]())
      .rekeningen(List(buildRekening("r1", 0, 0, 0)))
      .boekingen(List[Boeking]())
      .build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie);
    regels.stream().forEach(f => println(f.omschrijving));
    assertTrue("", regels.size == 0)
  }

  @Test
  def when_one_factuur_then_return_geen_errors(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List[Afschrift]())
      .facturen(List(buildFactuur("f1", 0, 0)))
      .rekeningen(List[Rekening]())
      .boekingen(List[Boeking]())
      .build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie);
    regels.stream().forEach(f => println(f.omschrijving));
    assertTrue("", regels.size == 0)
  }

  @Test
  def when_dubbele_rekeningen_then_return_een_error(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List[Afschrift]())
      .facturen(List[Factuur]())
      .rekeningen(List(buildRekening("r1", 0, 0, 0), buildRekening("r1", 0, 0, 0)))
      .boekingen(List[Boeking]())
      .build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie);
    regels.stream().forEach(f => println(f.omschrijving));
    assertTrue("", regels.size == 1)
    assertTrue("", regels(0).rubriceerAction == FixAction.NONE)
    assertTrue("", regels(0).checkType == CheckType.WARNING)
  }

  @Test
  def when_dubbele_facturen_then_return_een_error(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List[Afschrift]())
      .facturen(List(buildFactuur("f1", 0, 0), buildFactuur("f1", 0, 0)))
      .rekeningen(List[Rekening]())
      .boekingen(List[Boeking]())
      .build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie);
    regels.stream().forEach(f => println(f.omschrijving));
    assertTrue("", regels.size == 1)
    assertTrue("", regels(0).rubriceerAction == FixAction.NONE)
    assertTrue("", regels(0).checkType == CheckType.WARNING)
  }

  @Test
  def when_dubbele_afschriften_then_return_een_error(): Unit = {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 0), buildAfschrift("a1", 0)))
      .facturen(List[Factuur]())
      .rekeningen(List[Rekening]())
      .boekingen(List[Boeking]())
      .build()

    val regels = CheckServiceScala.getCheckAndFixRegels(administratie);
    regels.stream().forEach(f => println(f.omschrijving));
    assertTrue("", regels.size == 1)
    assertTrue("", regels(0).rubriceerAction == FixAction.NONE)
    assertTrue("", regels(0).checkType == CheckType.WARNING)
  }


  def factuurBoeking(uuid: String, factuurNr: String, afschriftNummer: String): BetaaldeFactuurBoeking = {
    BetaaldeFactuurBoeking.newBuilder()
      .uuid(uuid)
      .afschriftNummer(afschriftNummer)
      .factuurNummer(factuurNr)
      .build()
  }

  def rekeningBoeking(uuid: String, rekeningNr: String, afschriftNummer: String): BetaaldeRekeningBoeking = {
    BetaaldeRekeningBoeking.newBuilder()
      .uuid(uuid)
      .afschriftNummer(afschriftNummer)
      .rekeningNummer(rekeningNr)
      .build()
  }

  def buildFactuur(nummer: String, ex: Double, perc: Double): Factuur = {
    Factuur.newBuilder()
      .bedragExBtw(ex)
      .factuurNummer(nummer)
      .factuurRegels(FactuurRegel.newBuilder().aantal(1).stuksPrijs(ex).btwPercentage(perc).build()::Nil)
      .build()
  }

  def buildAfschrift(nummer: String, bedrag: Double): Afschrift = {
    Afschrift.newBuilder()
      .nummer(nummer)
      .bedrag(bedrag)
      .build()
  }

  def buildRekening(nummer: String, inc: Double, ex: Double, btw: Double): Rekening = {
    Rekening.newBuilder()
      .bedragExBtw(ex)
      .bedragIncBtw(inc)
      .btw(btw)
      .rekeningNummer(nummer)
      .build()
  }

}
