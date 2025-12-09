package server;

import com.google.gson.Gson;
import dataaccess.*;
import dataaccess.daomodels.AuthDAO;
import dataaccess.daomodels.GameDAO;
import dataaccess.daomodels.UserDAO;
import dataaccess.memorydaos.MemoryAuthDAO;
import dataaccess.memorydaos.MemoryGameDAO;
import dataaccess.memorydaos.MemoryUserDAO;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import exception.ResponseException;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;
import model.UserData;

public class Server {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final CreateGameService createGameService;
    private final JoinGameService joinGameService;
    private final ListGameService listGameService;

    private final Javalin javalin;

    public Server() {

        boolean sql = true;

        if (sql) {
            try {
                DatabaseManager.configureDatabase();
            } catch (DataAccessException | ResponseException ex) {
                throw new RuntimeException("configureDatabase failed", ex);
            }
        }
        userDAO = sql ? new SQLUserDAO() : new MemoryUserDAO();
        authDAO = sql ? new SQLAuthDAO() : new MemoryAuthDAO();
        gameDAO = sql ? new SQLGameDAO() : new MemoryGameDAO();
        registerService = new RegisterService(authDAO, userDAO);
        loginService = new LoginService(authDAO, userDAO);
        logoutService = new LogoutService(authDAO);
        createGameService = new CreateGameService(authDAO, gameDAO);
        joinGameService = new JoinGameService(authDAO, gameDAO);
        listGameService = new ListGameService(authDAO, gameDAO);
        javalin = Javalin.create(cfg -> {
                    cfg.staticFiles.add("web");
                    cfg.http.defaultContentType = "application/json";
                })
                .post("/user", this::addUser)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .post("/game", this::addGame)
                .put("/game", this::joinGame)
                .get("/game", this::listGames)
                .delete("/db", this::clearDB)
                .exception(ResponseException.class, this::responseExceptionHandler);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private void addUser(Context ctx) throws DataAccessException, ResponseException {
        var user = new Gson().fromJson(ctx.body(), UserData.class);
        if (user == null || user.username() == null || user.password() == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        var auth = registerService.createUser(user.username(), user.password(), user.email());
        ctx.result(new Gson().toJson(auth));
    }

    private void login(Context ctx) throws DataAccessException, ResponseException {
        var user = new Gson().fromJson(ctx.body(), LoginRequest.class);
        if (user == null || user.username() == null || user.password() == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        var auth = loginService.loginUser(user.username(), user.password());
        ctx.result(new Gson().toJson(auth));
    }

    private void logout(Context ctx) throws DataAccessException, ResponseException {
        String token = ctx.header("authorization");
        if (token == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        logoutService.logoutUser(token);
        ctx.status(200).result("");
    }

    private void addGame(Context ctx) throws DataAccessException, ResponseException {
        var token = ctx.header("authorization");
        if (token == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        var name = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        var game = createGameService.createGame(token, name.gameName());
        var response = String.format("{\"gameID\": %d}", game.gameID());
        ctx.status(200).result(response);
    }

    private void joinGame(Context ctx) throws DataAccessException, ResponseException {
        var token = ctx.header("authorization");
        if (token == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        var input = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        Integer gameID = input.gameID();
        String color = input.playerColor();
        joinGameService.joinGame(token, color, gameID);
    }

    private void listGames(Context ctx) throws DataAccessException, ResponseException {
        var token = ctx.header("authorization");
        if (token == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
        }
        var response = listGameService.listGames(token);
        ctx.status(200).result(new Gson().toJson(response));
    }

    private void clearDB(Context ctx) throws ResponseException {
        try {
            gameDAO.clearGames();
            userDAO.clearUsers();
            authDAO.clearAuths();
            ctx.status(200);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void stop() {
        javalin.stop();
    }

    private void responseExceptionHandler(ResponseException ex, Context ctx) {
        ctx.status(ex.toHttpStatusCode());
        ctx.result(ex.toJson());
    }
}
