package com.vdzon.administratie.checkandfix.actions.check


import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.model.boekingen.{BetaaldeFactuurBoeking, Boeking}
import com.vdzon.administratie.model.{BoekingenCache, Factuur, Afschrift}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import collection.mutable.Stack
import collection.JavaConversions._
import scala.compat.java8.StreamConverters._

@RunWith(classOf[JUnitRunner])
class AfschriftCheckTest extends FlatSpec with Matchers {

//  "The AfschriftCheck" should "detect a problem" in {
//    var checkAndFixData = new CheckAndFixData();
//    checkAndFixData.alleAfschriften = List(new Afschrift(),new Afschrift())
//    checkAndFixData.alleFacturen = List(new Factuur(),new Factuur())
//    checkAndFixData.alleBoekingen = List(new BetaaldeFactuurBoeking(),new BetaaldeFactuurBoeking())
//    checkAndFixData.boekingenCache = new BoekingenCache(checkAndFixData.alleBoekingen);
//
//    val regels = new AfschriftCheck().checkOfAfschriftNogBestaat(checkAndFixData);
//    regels.size() should be (1)
//  }
//
//  it should "throw NoSuchElementException if an empty stack is popped" in {
//    val emptyStack = new Stack[Int]
//    a [NoSuchElementException] should be thrownBy {
//      emptyStack.pop()
//    }
//  }

}
