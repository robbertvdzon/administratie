package com.vdzon.administratie.mongo

import com.github.mongobee.exception.MongobeeException
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.vdzon.administratie.model.Gebruiker
//import com.vdzon.administratie.util.LocalDateTimeConverter
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import java.util.*
import com.mongodb.MongoCredential.createScramSha1Credential


//import org.mongodb.morphia.Datastore
//import org.mongodb.morphia.Morphia
//import org.mongodb.morphia.query.Query

class Mongo @Throws(MongobeeException::class)
private constructor() {

    lateinit var datastore: MongoDatabase   //normal java driver usage
        private set

    init {
        init()
        createAdminUserWhenNotExists()
    }

    private fun createAdminUserWhenNotExists() {
        val gebruikers: MongoCollection<Gebruiker> = this.datastore!!.getCollection<Gebruiker>()
        if (gebruikers.count().equals(0)) {
            TestDataGenerator.buildTestData("admin", "admin", "admin", true, datastore)
        }
    }

    @Throws(MongobeeException::class)
    fun init() {

//        val morphia = Morphia()
//        morphia.mapper.converters.addConverter(LocalDateTimeConverter())
//        morphia.mapper.converters.addConverter(BigDecimalConverter::class.java)
        println("connect to mongo")
//        morphia.mapPackage("org.mongodb.morphia.example")

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

//        val uri = MongoClientURI(url)
//        val mongoClient = MongoClient(uri)
//        datastore = morphia.createDatastore(mongoClient, dbName)
//        datastore!!.ensureIndexes()

        val serverAddress:ServerAddress = ServerAddress(host,port.toInt())
        val credentias = MongoCredential.createCredential(username,"admin",passwd.toCharArray())


        val credentials:List<MongoCredential> = Arrays.asList(credentias)
//        val client = KMongo.createClient(url) //get com.mongodb.MongoClient new instance
        val client = KMongo.createClient(serverAddress,credentials) //get com.mongodb.MongoClient new instance
        datastore = client.getDatabase(dbName)


        UpdateMongo().start(client, dbName)
    }

    companion object {
        lateinit var mongo: Mongo
            private set

        @Throws(MongobeeException::class)
        fun start() {
            mongo = Mongo()

        }
    }
}
