package com.vdzon.administratie.auth;

import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;
import spark.Session;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthService {

    public static final String AUTHENTICATED_USER_UUID = "authenticatedUserUuid";

    @Inject
    UserCrud userCrud;

    protected Object login(Request req, Response res) throws Exception{
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        Gebruiker gebruiker = userCrud.getGebruikerByUsername(username);
        if (gebruiker == null || !gebruiker.authenticate(password)){
            res.status(401);
            SessionHelper.removeAuthenticatedUserUuid(req);
            return new SingleAnswer("not authorized");
        }
        SessionHelper.setAuthenticatedUserUuid(req,gebruiker.getUuid());
        return new SingleAnswer("ok");
    }

    protected Object logout(Request req, Response res) throws Exception{
        SessionHelper.removeAuthenticatedUserUuid(req);
        return new SingleAnswer("ok");
    }

    protected Object getcurrentuser(Request req, Response res) throws Exception{
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        if (uuid == null){
            res.status(404);
            return new SingleAnswer("not found");
        }
        return userCrud.getGebruiker(uuid);
    }

}
