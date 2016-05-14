package com.vdzon.administratie.contact;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class ContactResource {

    @Inject
    public ContactResource(ContactService factuurService) {
        Spark.put("/rest/contact/", factuurService::putContact, JsonUtil.json());
        Spark.post("/rest/contact/", factuurService::putContact, JsonUtil.json());
        Spark.delete("/rest/contact/:uuid", factuurService::removeContact, JsonUtil.json());
    }


}
