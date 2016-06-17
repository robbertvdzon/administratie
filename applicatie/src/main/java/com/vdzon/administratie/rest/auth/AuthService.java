package com.vdzon.administratie.rest.auth;

import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class AuthService {

    @Inject
    UserCrud userCrud;

    protected Object register(Request req, Response res) throws Exception {
        String name = req.queryParams("name");
        String username = req.queryParams("username");
        String password = req.queryParams("password");

        Gebruiker gebruiker = userCrud.getGebruikerByUsername(username);

        if (gebruiker != null ) {
            res.status(401);
            SessionHelper.removeAuthenticatedUserUuid(req);
            return new SingleAnswer("username bestaat al");
        }

        gebruiker = Gebruiker
                .builder()
                .admin(false)
                .name(name)
                .password(password)
                .username(username)
                .uuid(UUID.randomUUID().toString()).build();
        gebruiker.initDefaultAdministratie();
        try {
            userCrud.updateGebruiker(gebruiker);
            SessionHelper.setAuthenticatedUserUuid(req, gebruiker.getUuid());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new SingleAnswer("ok");
    }

    protected Object login(Request req, Response res) throws Exception {
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        Gebruiker gebruiker = userCrud.getGebruikerByUsername(username);
        if (gebruiker == null || !gebruiker.authenticate(password)) {
            res.status(401);
            SessionHelper.removeAuthenticatedUserUuid(req);
            return new SingleAnswer("not authorized");
        }
        SessionHelper.setAuthenticatedUserUuid(req, gebruiker.getUuid());
        return new SingleAnswer("ok");
    }

    protected Object logout(Request req, Response res) throws Exception {
        SessionHelper.removeAuthenticatedUserUuid(req);
        return new SingleAnswer("ok");
    }

}
