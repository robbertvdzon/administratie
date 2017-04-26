package com.vdzon.administratie

import com.google.inject.Injector
import com.vdzon.administratie.rest.version.VersionData
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object AppUtil{
    fun loadApplicationVersion(injector: Injector) {
        val versionData = injector.getInstance(VersionData::class.java)
        try {
            val resources = App::class.java.classLoader.getResources("META-INF/MANIFEST.MF")
            // walk through all manifest files (for each included jar there is a manifest,
            // we need find ours by checking the mainClass
            while (resources.hasMoreElements()) {
                val url = resources.nextElement()
                val properties = Properties()
                properties.load(url.openStream())
                val mainClass = properties.getProperty("Main-Class")
                if (mainClass != null && mainClass == App::class.java.canonicalName) {
                    //Correct manifest found
                    versionData.version = properties.getProperty("Implementation-Version")
                    versionData.buildTime = reformatBuildTime(properties.getProperty("Build-Time"))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun reformatBuildTime(buildTime: String): String {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(buildTime.replace("Z$".toRegex(), "+0000"))
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return "Unknown"
    }
}