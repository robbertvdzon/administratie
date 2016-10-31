package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.{Afschrift, BoekingenCache, Factuur, Rekening}
import scala.collection.JavaConversions._
import scala.compat.java8.StreamConverters._

case class CheckAndFixData(
                  alleAfschriften: List[Afschrift],
                  alleRekeningen: List[Rekening],
                  alleFacturen: List[Factuur],
                  alleBoekingen: List[Boeking],
                  boekingenCache: BoekingenCache)
