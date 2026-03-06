package server;

import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;
import dataaccess.daomodels.UserDao;
import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryGameDao;
import dataaccess.memory.MemoryUserDao;
import exception.ResponseException;
import handlers.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;

public class Server {

    private final Javalin javalin;
    private final AuthDao authDao;
    private final UserDao userDao;
    private final GameDao gameDao;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final CreateGameService createGameService;
    private final JoinGameService joinGameService;

    public Server() {
        this.userDao = new MemoryUserDao();
        this.authDao = new MemoryAuthDao();
        this.gameDao = new MemoryGameDao();
        this.registerService = new RegisterService(authDao, userDao);
        this.loginService = new LoginService(authDao, userDao);
        this.logoutService = new LogoutService(authDao);
        this.createGameService = new CreateGameService(authDao, gameDao);
        this.joinGameService = new JoinGameService(authDao, gameDao);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))

                .post("/user", new RegisterHandler(registerService))
                .post("/session", new LoginHandler(loginService))
                .delete("/session", new LogoutHandler(logoutService))
                .post("/game", new CreateGameHandler(createGameService))
                .put("/game", new JoinGameHandler(joinGameService))
                .exception(ResponseException.class, this::exceptionHandler);

        // Register your endpoints and exception handlers here.

    }

    private void exceptionHandler(ResponseException ex, Context ctx) {
        ctx.status(ex.toHttpStatusCode());
        ctx.result(ex.toJson());
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
