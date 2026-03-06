package handlers;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ListGamesService;

public class ListGamesHandler implements Handler {

    private final ListGamesService listGamesService;

    public ListGamesHandler(ListGamesService listGamesService) {
        this.listGamesService = listGamesService;
    }

    public void handle(Context ctx) throws ResponseException {
        String token = ctx.header("Authorization");
        if (token == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        ctx.status(200).result(new Gson().toJson(listGamesService.listGames(token)));
    }
}
