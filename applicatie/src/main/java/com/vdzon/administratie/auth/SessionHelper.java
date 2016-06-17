package com.vdzon.administratie.auth;

import com.vdzon.administratie.checkandfix.rest.ForbiddenException;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.Gebruiker;
import spark.Request;
import spark.Session;

public class SessionHelper {

    private static final String AUTHENTICATED_USER_UUID = "authenticatedUserUuid";

    public static String getAuthenticatedUserUuid(Request req) {
        Session session = req.session(true);
        String uuid = session.<String>attribute(AUTHENTICATED_USER_UUID);
        return uuid;
    }

    public static void setAuthenticatedUserUuid(Request req, String uuid) {
        Session session = req.session(true);
        session.attribute(AUTHENTICATED_USER_UUID, uuid);
    }

    public static void removeAuthenticatedUserUuid(Request req) {
        Session session = req.session(true);
        session.removeAttribute(AUTHENTICATED_USER_UUID);
    }

    public static Gebruiker getGebruikerOrThowForbiddenExceptin(Request req, UserCrud crudService) {
        String uuid = getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null) {
            throw new ForbiddenException();
        }
        return gebruiker;

    }
}
