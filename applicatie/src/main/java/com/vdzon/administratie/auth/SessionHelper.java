package com.vdzon.administratie.auth;

import spark.Request;
import spark.Session;

public class SessionHelper {

    private static final String AUTHENTICATED_USER_UUID = "authenticatedUserUuid";

    public static String getAuthenticatedUserUuid(Request req) {
        Session session = req.session(true);
        String id = session.id();
        boolean aNew = session.isNew();
        System.out.println("get:sessie "+id+" "+aNew);
        String uuid = session.<String>attribute(AUTHENTICATED_USER_UUID);
        return uuid;
    }

    public static void setAuthenticatedUserUuid(Request req, String uuid) {
        Session session = req.session(true);
        String id = session.id();
        boolean aNew = session.isNew();
        System.out.println("set:sessie "+id+" "+aNew);
        session.attribute(AUTHENTICATED_USER_UUID, uuid);
    }

    public static void removeAuthenticatedUserUuid(Request req) {
        Session session = req.session(true);
        String id = session.id();
        boolean aNew = session.isNew();
        System.out.println("remove:sessie "+id+" "+aNew);
        session.removeAttribute(AUTHENTICATED_USER_UUID);
    }


}
