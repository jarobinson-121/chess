package handlers;

import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearDBService;

public class ClearDBHandler implements Handler {

    private final ClearDBService clearDBService;

    public ClearDBHandler(ClearDBService clearDBService) {
        this.clearDBService = clearDBService;
    }

    public void handle(Context ctx) throws ResponseException {
        clearDBService.clearDB();
    }
}
