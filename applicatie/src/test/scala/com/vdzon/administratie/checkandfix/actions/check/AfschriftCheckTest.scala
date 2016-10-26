package com.vdzon.administratie.checkandfix.actions.check


import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.model.boekingen.{BetaaldeFactuurBoeking, Boeking}
import com.vdzon.administratie.model.{Rekening, BoekingenCache, Factuur, Afschrift}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import collection.mutable.Stack
import collection.JavaConversions._
import scala.compat.java8.StreamConverters._

@RunWith(classOf[JUnitRunner])
class AfschriftCheckTest extends FlatSpec with Matchers {

  "The AfschriftCheck" should "detect when facturen niet volledig geboekt" in {
    var checkAndFixData: CheckAndFixData = buildDataWithOngelijkeBedragenInFactuur
    val regels = new AfschriftCheck().checkOfAfschriftNogBestaat(checkAndFixData);
    regels.size() should be (1)
  }

  it should "detect when facturen volledig geboekt" in {
    var checkAndFixData: CheckAndFixData = buildDataWithGelijkeBedragenInFactuur
    val regels = new AfschriftCheck().checkOfAfschriftNogBestaat(checkAndFixData);
    regels.size() should be (0)
  }

  def buildDataWithOngelijkeBedragenInFactuur: CheckAndFixData = {
    var checkAndFixData = new CheckAndFixData();
    checkAndFixData.alleAfschriften = List(buildAfschrift("a1", 100), buildAfschrift("a2", 101))
    checkAndFixData.alleFacturen = List(buildFactuur("f1", 100, 80, 20), buildFactuur("f2", 100, 80, 20))
    checkAndFixData.alleRekeningen = List[Rekening]()
    checkAndFixData.alleBoekingen = List(factuurBoeking("f1", "a1"), factuurBoeking("f2", "a2"));
    checkAndFixData.boekingenCache = new BoekingenCache(checkAndFixData.alleBoekingen);
    checkAndFixData
  }

  def buildDataWithGelijkeBedragenInFactuur: CheckAndFixData = {
    var checkAndFixData = new CheckAndFixData();
    checkAndFixData.alleAfschriften = List(buildAfschrift("a1", 100), buildAfschrift("a2", 100))
    checkAndFixData.alleFacturen = List(buildFactuur("f1", 100, 80, 20), buildFactuur("f2", 100, 80, 20))
    checkAndFixData.alleRekeningen = List[Rekening]()
    checkAndFixData.alleBoekingen = List(factuurBoeking("f1", "a1"), factuurBoeking("f2", "a2"));
    checkAndFixData.boekingenCache = new BoekingenCache(checkAndFixData.alleBoekingen);
    checkAndFixData
  }

  def factuurBoeking(factuurNr:String, afschriftNummer:String): BetaaldeFactuurBoeking = {
    BetaaldeFactuurBoeking.newBuilder().afschriftNummer(afschriftNummer).factuurNummer(factuurNr).build()
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
}
