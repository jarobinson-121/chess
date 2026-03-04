package handlers;

import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.LogoutService;

public class LogoutHandler implements Handler {

    private final LogoutService logoutService;

    public LogoutHandler(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    public void handle(Context ctx) throws ResponseException {
        String token = ctx.header("authorization");
        if (token == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Bad Request");
        }

    }
}
