package com.vdzon.administratie.database

import com.github.mongobee.exception.MongobeeException
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.vdzon.administratie.model.Gebruiker
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.litote.kmongo.toList
import java.util.*
import java.io.FileOutputStream
import java.io.ObjectOutputStream



class MongoDatabase : AdministratieDatabase{

    lateinit var datastore: MongoDatabase

    override fun startDatabase() {
        init()
        createAdminUserWhenNotExists()
    }


    @Throws(MongobeeException::class)
    fun init() {
        println("connect to mongo")

        val mongoDbPort = System.getProperties().getProperty("mongoDbPort")
        val mongoDbHostname = System.getProperties().getProperty("mongoDbHost")
        val mongoDbUsername = System.getProperties().getProperty("mongoDbUsername")
        val mongoDbPasswd = System.getProperties().getProperty("mongoDbPasswd")
        val mongoDbName = System.getProperties().getProperty("mongoDbName")

        println("Check parameters:")
        println("mongoDbPort:" + mongoDbPort)
        println("mongoDbHost:" + mongoDbHostname)
        println("mongoDbUsername:" + mongoDbUsername)
        println("mongoDbPasswd:" + mongoDbPasswd)
        println("mongoDbName:" + mongoDbName)
        println("usage voorbeeld: -DmongoDbPort=9903 -DmongoDbHost=\"37.97.149.78\" -DmongoDbUsername=\"robbert\" -DmongoDbPasswd=\"mypasswd\" -DmongoDbName=\"mijndb\"")

        val host = mongoDbHostname ?: "localhost"
        val port = mongoDbPort ?: "27017"
        val username = mongoDbUsername ?: ""
        val passwd = mongoDbPasswd ?: ""
        val dbName = mongoDbName ?: "zzp-administratie"

        val usernamePasswd = if (username.length == 0) "" else "$username:$passwd@"
        val url = "mongodb://$usernamePasswd$host:$port"
        println("Login using: " + url)

        val serverAddress: ServerAddress = ServerAddress(host, port.toInt())
        val credentias = MongoCredential.createCredential(username, "admin", passwd.toCharArray())


        val credentials: List<MongoCredential> = Arrays.asList(credentias)
        val client = KMongo.createClient(serverAddress, credentials) //get com.mongodb.MongoClient new instance
        datastore = client.getDatabase(dbName)

        UpdateMongo().start(client, dbName)
    }

    private fun createAdminUserWhenNotExists() {
        val gebruikers: MongoCollection<Gebruiker> = this.datastore!!.getCollection<Gebruiker>()
        if (gebruikers.count().equals(0)) {
            TestDataGenerator.buildTestData("admin", "admin", true, datastore)
        }
    }



}
