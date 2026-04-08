package server;

import dataaccess.DatabaseManager;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;
import dataaccess.daomodels.UserDao;
import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryGameDao;
import dataaccess.memory.MemoryUserDao;
import dataaccess.sql.SQLAuthDao;
import dataaccess.sql.SQLGameDao;
import dataaccess.sql.SQLUserDao;
import exception.ResponseException;
import handlers.*;
import io.javalin.*;
import io.javalin.http.Context;
import server.websocket.WebSocketHandler;
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
    private final ListGamesService listGamesService;
    private final ClearDBService clearDBService;
    private final WebSocketHandler webSocketHandler;

    public Server() {

        boolean sql = true;

        if (sql) {
            try {
                DatabaseManager.configureDatabase();
            } catch (Exception ex) {
                throw new RuntimeException("Database Configuration failed", ex);
            }
        }

        this.userDao = (sql) ? new SQLUserDao() : new MemoryUserDao();
        this.authDao = (sql) ? new SQLAuthDao() : new MemoryAuthDao();
        this.gameDao = (sql) ? new SQLGameDao() : new MemoryGameDao();
        this.registerService = new RegisterService(authDao, userDao);
        this.loginService = new LoginService(authDao, userDao);
        this.logoutService = new LogoutService(authDao);
        this.createGameService = new CreateGameService(authDao, gameDao);
        this.joinGameService = new JoinGameService(authDao, gameDao);
        this.listGamesService = new ListGamesService(authDao, gameDao);
        this.clearDBService = new ClearDBService(authDao, gameDao, userDao);
        this.webSocketHandler = new WebSocketHandler();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))

                .post("/user", new RegisterHandler(registerService))
                .post("/session", new LoginHandler(loginService))
                .delete("/session", new LogoutHandler(logoutService))
                .post("/game", new CreateGameHandler(createGameService))
                .put("/game", new JoinGameHandler(joinGameService))
                .get("/game", new ListGamesHandler(listGamesService))
                .delete("/db", new ClearDBHandler(clearDBService))
                .exception(ResponseException.class, this::exceptionHandler)
                .ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });

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
