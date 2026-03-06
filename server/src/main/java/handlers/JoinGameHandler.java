package handlers;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import requests.JoinGameRequest;
import service.JoinGameService;

public class JoinGameHandler implements Handler {

    private final JoinGameService joinGameService;

    public JoinGameHandler(JoinGameService joinGameService) {
        this.joinGameService = joinGameService;
    }

    public void handle(Context ctx) throws ResponseException {
        String token = ctx.header("authorization");
        if (token == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        JoinGameRequest request = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        String playerColor = request.playerColor();
        int gameID = request.gameID();
        joinGameService.joinGame(token, playerColor, gameID);
    }
}
