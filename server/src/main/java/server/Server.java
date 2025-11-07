package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import io.javalin.*;
import io.javalin.http.Context;
import service.CreateGameService;
import service.LoginService;
import service.LogoutService;
import service.RegisterService;
import model.UserData;

public class Server {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final CreateGameService createGameService;

    private final Javalin javalin;

    public Server() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        registerService = new RegisterService(authDAO, userDAO);
        loginService = new LoginService(authDAO, userDAO);
        logoutService = new LogoutService(authDAO);
        createGameService = new CreateGameService(authDAO, gameDAO);
        javalin = Javalin.create(cfg -> {
                    cfg.staticFiles.add("web");
                    cfg.http.defaultContentType = "application/json";
                })
                .post("/user", this::addUser)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .post("/game", this::addGame)
                .exception(ResponseException.class, this::exceptionHandler);

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private void addUser(Context ctx) throws DataAccessException, ResponseException {
        var user = new Gson().fromJson(ctx.body(), UserData.class);
        if (user == null || user.username() == null || user.username().isBlank()
                || user.password() == null || user.password().isBlank()) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        var auth = registerService.createUser(user.username(), user.password(), user.email());
        ctx.result(new Gson().toJson(auth));
    }

    private void login(Context ctx) throws DataAccessException, ResponseException {
        var user = new Gson().fromJson(ctx.body(), LoginRequest.class);
        if (user == null || user.username() == null || user.username().isBlank()
                || user.password() == null || user.password().isBlank()) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        var auth = loginService.loginUser(user.username(), user.password());
        ctx.result(new Gson().toJson(auth));
    }

    private void logout(Context ctx) throws DataAccessException, ResponseException {
        String token = ctx.header("authorization");
        if (token == null || token.isBlank()) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        logoutService.logoutUser(token);
        ctx.status(200).result("");
    }

    private void addGame(Context ctx) throws DataAccessException {
        var token = ctx.header("authorization");
        if (token == null || token.isBlank()) {
            ctx.status(400).result("Error: bad request");
            return;
        }
        var name = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        var game = createGameService.createGame(token, name.gameName());
        var response = String.format("{\"gameID\": %d}", game.gameID());
        ctx.status(200).result(response);
    }

    public void stop() {
        javalin.stop();
    }

    private void exceptionHandler(ResponseException ex, Context ctx) {
        ctx.status(ex.toHttpStatusCode());
        ctx.result(ex.toJson());
    }
}
