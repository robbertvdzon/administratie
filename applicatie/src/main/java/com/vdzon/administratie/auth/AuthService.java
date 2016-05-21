package com.vdzon.administratie.auth;

import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthService {

    @Inject
    UserCrud userCrud;

    protected Object login(Request req, Response res) throws Exception {
        try {
            System.out.println("login 1");
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            Gebruiker gebruiker = userCrud.getGebruikerByUsername(username);
            System.out.println("login 2 " + gebruiker);
            if (gebruiker == null || !gebruiker.authenticate(password)) {
                res.status(401);
                SessionHelper.removeAuthenticatedUserUuid(req);
                return new SingleAnswer("not authorized");
            }
            System.out.println("login 3");
            SessionHelper.setAuthenticatedUserUuid(req, gebruiker.getUuid());
            System.out.println("login 4");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
            return new SingleAnswer("ok");
    }

    protected Object logout(Request req, Response res) throws Exception {
        SessionHelper.removeAuthenticatedUserUuid(req);
        return new SingleAnswer("ok");
    }

}
