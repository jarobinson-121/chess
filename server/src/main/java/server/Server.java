package server;

import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;
import dataaccess.daomodels.UserDao;
import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryGameDao;
import dataaccess.memory.MemoryUserDao;
import handlers.RegisterHandler;
import io.javalin.*;
import service.RegisterService;

public class Server {

    private final Javalin javalin;
    private final AuthDao authDao;
    private final UserDao userDao;
    private final GameDao gameDao;
    private final RegisterService registerService;


    public Server() {
        this.userDao = new MemoryUserDao();
        this.authDao = new MemoryAuthDao();
        this.gameDao = new MemoryGameDao();
        this.registerService = new RegisterService(authDao, userDao);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))

                .post("/user", new RegisterHandler(registerService));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
