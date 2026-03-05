package handlers;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.AuthData;
import models.UserData;
import requests.LoginRequest;
import service.LoginService;

public class LoginHandler implements Handler {

    private final LoginService loginService;

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    public void handle(Context ctx) throws ResponseException {
        var user = new Gson().fromJson(ctx.body(), LoginRequest.class);
        if (user == null || user.username() == null || user.password() == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        AuthData auth = loginService.loginUser(user.username(), user.password());
        ctx.result(new Gson().toJson(auth));
    }

}
