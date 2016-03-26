package com.vdzon.weegschaal.auth;

import com.vdzon.weegschaal.crud.UserCrud;
import com.vdzon.weegschaal.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthService {
    private boolean authenticated = false;

    @Inject
    UserCrud userCrud;

    protected Object login(Request req, Response res) throws Exception{
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        if (!username.equals("q")){
            res.status(401);
            authenticated = false;
            return new SingleAnswer("not authorized");
        }
        authenticated = true;
        return new SingleAnswer("ok");
    }

    protected Object logout(Request req, Response res) throws Exception{
        authenticated = false;
        return new SingleAnswer("ok");
    }

    protected Object getcurrentuser(Request req, Response res) throws Exception{
        if (!authenticated){
            res.status(404);
            return new SingleAnswer("not found");
        }
        return userCrud.getCustomer("");
    }

}
