package com.vdzon.administratie.checkandfix.actions.check

import java.util.Collection

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel

class AfschriftCheck {
  @AdministratieCheckRule
  def checkOfAfschriftNogBestaat(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] = {
    println("CHECK VANUIT SCALA");
    return null;
  }

}
