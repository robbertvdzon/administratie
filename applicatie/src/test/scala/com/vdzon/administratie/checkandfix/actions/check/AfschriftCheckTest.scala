package com.vdzon.administratie.checkandfix.actions.check


import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.model.{CheckType, FixAction}
import com.vdzon.administratie.model.boekingen.{BetaaldeRekeningBoeking, BetaaldeFactuurBoeking, Boeking}
import com.vdzon.administratie.model._
import com.vdzon.administratie.rest.checkandfix.CheckAndFixService
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import collection.mutable.Stack
import collection.JavaConversions._
import scala.compat.java8.StreamConverters._

@RunWith(classOf[JUnitRunner])
class AfschriftCheckTest extends FlatSpec with Matchers {
  "The AfschriftCheck" should "detect when facturen niet volledig geboekt" in {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100), buildAfschrift("a2", 101)))
      .facturen(List(buildFactuur("f1", 100, 80, 20), buildFactuur("f2", 100, 80, 20)))
      .rekeningen(List[Rekening]())
      .boekingen(List(factuurBoeking("b1" ,"f1", "a1"), factuurBoeking("b2" ,"f2", "a2"))).build()
    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.size() should be (1)
  }

  it should "detect when facturen en rekeningen volledig geboekt" in {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100), buildAfschrift("a2", 100), buildAfschrift("a3", -100)))
      .facturen(List(buildFactuur("f1", 100, 80, 20), buildFactuur("f2", 100, 80, 20)))
      .rekeningen(List(buildRekening("r1",100,80,20 )))
      .boekingen(List(factuurBoeking("b1" ,"f1", "a1"), factuurBoeking("b2" ,"f2", "a2"), rekeningBoeking("b3","r1","a3"))).build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (0)
  }

  it should "detect when boeking contain non-existing factuur" in {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100), buildAfschrift("a2", 100)))
      .facturen(List(buildFactuur("f1", 100, 80, 20)))
      .rekeningen(List[Rekening]())
      .boekingen(List(factuurBoeking("b1" ,"f1", "a1"), factuurBoeking("b2" ,"f2", "a2"))).build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (1)
    regels(0).getRubriceerAction should be (FixAction.REMOVE_BOEKING)
    regels(0).getCheckType should be (CheckType.FIX_NEEDED)
    regels(0).getBoekingUuid should be ("b2")
  }

  it should "detect when boeking contain non-existing rekening" in {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100)))
      .facturen(List[Factuur]())
      .rekeningen(List[Rekening]())
      .boekingen(List(rekeningBoeking("b1" ,"r1", "a1"))).build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (1)
    regels(0).getRubriceerAction should be (FixAction.REMOVE_BOEKING)
    regels(0).getCheckType should be (CheckType.FIX_NEEDED)
    regels(0).getBoekingUuid should be ("b1")
  }

  it should "detect when boeking contain non-existing afschrift" in {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100)))
      .facturen(List(buildFactuur("f1", 100, 80, 20),buildFactuur("f2", 0, 0, 0)))
      .rekeningen(List[Rekening]())
      .boekingen(List(factuurBoeking("b1" ,"f1", "a1"), factuurBoeking("b2" ,"f2", "a2"))).build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (1)
    regels(0).getRubriceerAction should be (FixAction.REMOVE_BOEKING)
    regels(0).getCheckType should be (CheckType.FIX_NEEDED)
    regels(0).getBoekingUuid should be ("b2")
  }

  it should "detect when rekening is niet volledig betaald" in {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 100)))
      .facturen(List[Factuur]())
      .rekeningen(List(buildRekening("r1",100,80,20)))
      .boekingen(List[Boeking]())
      .build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (1)
    regels(0).getRubriceerAction should be (FixAction.NONE)
    regels(0).getCheckType should be (CheckType.WARNING)
  }

  it should "give no error on one rekening" in {
    val administratie = Administratie.newBuilder
      .afschriften(List[Afschrift]())
      .facturen(List[Factuur]())
      .rekeningen(List(buildRekening("r1",0,0,0)))
      .boekingen(List[Boeking]())
      .build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (0)
  }

  it should "give no error on one factuur" in {
    val administratie = Administratie.newBuilder
      .afschriften(List[Afschrift]())
      .facturen(List(buildFactuur("f1", 0, 0, 0)))
      .rekeningen(List[Rekening]())
      .boekingen(List[Boeking]())
      .build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (0)
  }

  it should "detect when rekening hebben dubbele nummers" in {
    val administratie = Administratie.newBuilder
      .afschriften(List[Afschrift]())
      .facturen(List[Factuur]())
      .rekeningen(List(buildRekening("r1",0,0,0), buildRekening("r1",0,0,0)))
      .boekingen(List[Boeking]())
      .build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (1)
    regels(0).getRubriceerAction should be (FixAction.NONE)
    regels(0).getCheckType should be (CheckType.WARNING)
  }

  it should "detect when facturen hebben dubbele nummers" in {
    val administratie = Administratie.newBuilder
      .afschriften(List[Afschrift]())
      .facturen(List(buildFactuur("f1", 0, 0, 0),buildFactuur("f1", 0, 0, 0)))
      .rekeningen(List[Rekening]())
      .boekingen(List[Boeking]())
      .build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (1)
    regels(0).getRubriceerAction should be (FixAction.NONE)
    regels(0).getCheckType should be (CheckType.WARNING)
  }

  it should "detect when afschriften hebben dubbele nummers" in {
    val administratie = Administratie.newBuilder
      .afschriften(List(buildAfschrift("a1", 0), buildAfschrift("a1", 0)))
      .facturen(List[Factuur]())
      .rekeningen(List[Rekening]())
      .boekingen(List[Boeking]())
      .build()

    val regels = new CheckAndFixService().getCheckAndFixRegels(administratie);
    regels.stream().forEach(f=>println(f.getOmschrijving));
    regels.size() should be (1)
    regels(0).getRubriceerAction should be (FixAction.NONE)
    regels(0).getCheckType should be (CheckType.WARNING)
  }

  def factuurBoeking(uuid:String, factuurNr:String, afschriftNummer:String): BetaaldeFactuurBoeking = {
    BetaaldeFactuurBoeking.newBuilder()
      .uuid(uuid)
      .afschriftNummer(afschriftNummer)
      .factuurNummer(factuurNr)
      .build()
  }

  def rekeningBoeking(uuid:String, rekeningNr:String, afschriftNummer:String): BetaaldeRekeningBoeking = {
    BetaaldeRekeningBoeking.newBuilder()
      .uuid(uuid)
      .afschriftNummer(afschriftNummer)
      .rekeningNummer(rekeningNr)
      .build()
  }

  def buildFactuur(nummer:String, inc:Double, ex:Double, btw:Double): Factuur = {
    Factuur.newBuilder()
      .bedragExBtw(ex)
      .bedragIncBtw(inc)
      .btw(btw)
      .factuurNummer(nummer)
      .build()
  }

  def buildAfschrift(nummer:String, bedrag:Double): Afschrift = {
    Afschrift.newBuilder()
      .nummer(nummer)
      .bedrag(bedrag)
      .build()
  }

  def buildRekening(nummer:String, inc:Double, ex:Double, btw:Double): Rekening = {
    Rekening.newBuilder()
      .bedragExBtw(ex)
      .bedragIncBtw(inc)
      .btw(btw)
      .rekeningNummer(nummer)
      .build()
  }

}
