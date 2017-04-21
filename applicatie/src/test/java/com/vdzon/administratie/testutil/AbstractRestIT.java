package com.vdzon.administratie.testutil;

import com.jayway.restassured.response.Response;
import com.vdzon.administratie.App;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Before;
import spark.Spark;

public abstract class AbstractRestIT {
    private static String REST_PORT = "1122";
    private static int MONGO_PORT = 1133;
    MongodExecutable mongodExecutable = null;
    MongodProcess mongod = null;

    @Before
    public void setUp() throws Exception {
        setUpMongoDb();
        setUpApplication();
    }

    @After
    public void tearDown() throws Exception {
        tearDownMongoDb();
        tearDownApplication();
    }

    private void setUpApplication() throws Exception {
        App.INSTANCE.main(new String[]{REST_PORT, String.valueOf(MONGO_PORT)});
    }

    private void setUpMongoDb() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(MONGO_PORT, Network.localhostIsIPv6()))
                .build();
        mongodExecutable = starter.prepare(mongodConfig);
        mongod = mongodExecutable.start();
    }

    private void tearDownApplication() throws Exception {
        Spark.stop();
    }

    private void tearDownMongoDb() throws Exception {
        if (mongod != null){
            mongod.stop();
        }
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }

    }

    protected String getBaseUrl() {
        return "http://localhost:" + REST_PORT + "/";
    }

    public static <V>V getAndCheckResponse(Response response, Class clazz, int statusCode){
        response.then().statusCode(statusCode);
        return (V)response.then().extract().body().as(clazz);
    }


}