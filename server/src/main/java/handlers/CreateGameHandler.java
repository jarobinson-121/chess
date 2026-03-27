package handlers;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.GameData;
import requests.CreateGameRequest;
import service.CreateGameService;

public class CreateGameHandler implements Handler {

    private final CreateGameService createGameService;

    public CreateGameHandler(CreateGameService createGameService) {
        this.createGameService = createGameService;
    }

    public void handle(Context ctx) throws ResponseException {
        String token = ctx.header("Authorization");
        if (token == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        String gameName = new Gson().fromJson(ctx.body(), CreateGameRequest.class).gameName();
        if (gameName == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        GameData game = createGameService.createGame(token, gameName);
        String response = String.format("{\"gameID\": %d}", game.gameID());
        ctx.status(200).result(response);
    }
}
