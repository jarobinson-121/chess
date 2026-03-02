package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.AuthData;
import models.UserData;
import service.RegisterService;

public class RegisterHandler implements Handler {

    private final RegisterService registerService;

    public RegisterHandler(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    public void handle(Context ctx) throws ResponseException {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        if (user == null || user.username() == null || user.password() == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        AuthData auth = registerService.createUser(user);
        ctx.result(new Gson().toJson(auth));
    }
}
