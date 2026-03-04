package handlers;

import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.CreateGameService;

public class CreateGameHandler implements Handler {

    private final CreateGameService createGameService;

    public CreateGameHandler(CreateGameService createGameService) {
        this.createGameService = createGameService;
    }

    public void handle(Context ctx) throws ResponseException {
        String token = ctx.header("Authorization");
        if (token == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Bad Request");
        }

    }
}
