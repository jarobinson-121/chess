package server;

import com.google.gson.Gson;
import Service.RegisterService;
import Service.LoginService;
import Service.LogoutService;
import Service.CreateGameService;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.LoginRequest;
import dataaccess.CreateGameRequest;
import dataaccess.UserDAO;
import spark.*;
import model.UserData;

public class Server {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final CreateGameService createGameService;

    public Server(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO, RegisterService registerService, LoginService loginService, LogoutService logoutService, CreateGameService createGameService) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.registerService = registerService;
        this.loginService = loginService;
        this.logoutService = logoutService;
        this.createGameService = createGameService;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

//        Spark.delete("/db", this::clearDatabase);
        Spark.post("/user", this::addUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::addGame);

        Spark.awaitInitialization();
        return Spark.port();
    }
//    private Object clearDatabase(Request req, Response res) {
//
//    }

    private Object addUser(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = registerService.createUser(user.username(), user.password(), user.email());
        return new Gson().toJson(auth);
    }

    private Object login(Request req, Response res) throws DataAccessException {
        var userInfo = new Gson().fromJson(req.body(), LoginRequest.class);
        var auth = loginService.loginUser(userInfo.username(), userInfo.password());
        return new Gson().toJson(auth);
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        String token = req.headers("authorization");
        logoutService.logoutUser(token);
        return "";
    }

    private Object addGame(Request req, Response res) throws DataAccessException {
        var token = req.headers("authorization");
        var name = new Gson().fromJson(req.body(), CreateGameRequest.class);
        var game = createGameService.createGame(token, name.gameName());
        return new Gson().toJson(game.GameID());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}