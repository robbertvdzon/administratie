package com.vdzon.administratie

import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import com.google.inject.{Guice, Injector}
import com.vdzon.administratie.data.DataResource
import com.vdzon.administratie.rest.administratie.AdministratieResource
import com.vdzon.administratie.rest.afschrift.AfschriftResource
import com.vdzon.administratie.rest.auth.AuthResource
import com.vdzon.administratie.rest.bestelling.BestellingResource
import com.vdzon.administratie.rest.checkandfix.CheckAndFixResource
import com.vdzon.administratie.rest.contact.ContactResource
import com.vdzon.administratie.rest.declaratie.DeclaratieResource
import com.vdzon.administratie.rest.factuur.FactuurResource
import com.vdzon.administratie.rest.gebruiker.GebruikerResource
import com.vdzon.administratie.rest.overzicht.OverzichtResource
import com.vdzon.administratie.rest.rekening.RekeningResource
import com.vdzon.administratie.rest.rubriceren.RubriceerResource
import com.vdzon.administratie.rest.version.VersionResource
import spark.Spark

class AppScalaClass {
}

object AppScala {
  var version: String = "Undetermined"
  var buildTime: String = "Undetermined"

  def main(args: Array[String]) {
    loadVersion
    if (new File("C:\\workspace\\administratie\\applicatie\\src\\main\\resources\\web").exists) {
      Spark.externalStaticFileLocation("C:\\workspace\\administratie\\applicatie\\src\\main\\resources\\web")
    }
    else {
      Spark.staticFileLocation("/web")
    }

    Spark.exception(classOf[Exception], (exception, request, response) => {
      exception.printStackTrace();
      response.status(500);
      response.body(exception.getMessage());
    })
    if (args.length > 0) {
      Spark.port(args(0).toInt)
    }
    if (args.length > 1) {
      System.getProperties.setProperty("mongoDbPort", args(1))
    }

    Spark.before((request, response) => response.`type` ("application/json"))

    val injector: Injector = Guice.createInjector(new AppInjector)
    val dataResource: DataResource = injector.getInstance(classOf[DataResource])
    val authResource: AuthResource = injector.getInstance(classOf[AuthResource])
    val versionResource: VersionResource = injector.getInstance(classOf[VersionResource])
    val factuurResource: FactuurResource = injector.getInstance(classOf[FactuurResource])
    val contactResource: ContactResource = injector.getInstance(classOf[ContactResource])
    val gebruikerResource: GebruikerResource = injector.getInstance(classOf[GebruikerResource])
    val rekeningResource: RekeningResource = injector.getInstance(classOf[RekeningResource])
    val declaratieResource: DeclaratieResource = injector.getInstance(classOf[DeclaratieResource])
    val afschriftResource: AfschriftResource = injector.getInstance(classOf[AfschriftResource])
    val administratieResource: AdministratieResource = injector.getInstance(classOf[AdministratieResource])
    val bestellingResource: BestellingResource = injector.getInstance(classOf[BestellingResource])
    val rubriceerResource: RubriceerResource = injector.getInstance(classOf[RubriceerResource])
    val overzichtResource: OverzichtResource = injector.getInstance(classOf[OverzichtResource])
    val checkAndFixResource: CheckAndFixResource = injector.getInstance(classOf[CheckAndFixResource])
  }

  private def loadVersion {
      val resources: java.util.Enumeration[URL] = classOf[AppScalaClass].getClassLoader.getResources("META-INF/MANIFEST.MF")
      while (resources.hasMoreElements) {
        val url: URL = resources.nextElement
        val properties: Properties = new Properties
        properties.load(url.openStream)
        val mainClass: String = properties.getProperty("Main-Class")+"Class"

        if (mainClass != null && (mainClass == classOf[AppScalaClass].getCanonicalName)) {
          setVersion(properties.getProperty("Implementation-Version"))
          setBuildTime(reformatBuildTime(properties.getProperty("Build-Time")))
        }
      }
    System.out.println("Version is " + getVersion)
    System.out.println("Buildtime is " + getBuildTime)
  }

  def getVersion: String = {
    return AppScala.version
  }

  private def reformatBuildTime(buildTime: String): String = {
      val date: Date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")).parse(buildTime.replaceAll("Z$", "+0000"))
      return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date)
    return "Unknown"
  }

  def setVersion(version: String) {
    AppScala.version = version
  }

  def getBuildTime: String = {
    return AppScala.buildTime
  }

  def setBuildTime(buildTime: String) {
    AppScala.buildTime = buildTime
  }
}
