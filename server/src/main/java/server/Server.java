package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.LoginService;
import service.LogoutService;
import service.RegisterService;
import model.UserData;

public class Server {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final LogoutService logoutService;

    private final Javalin javalin;

    public Server() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        registerService = new RegisterService(authDAO, userDAO);
        loginService = new LoginService(authDAO, userDAO);
        logoutService = new LogoutService(authDAO);
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::addUser)
                .post("/session", this::login)
                .delete("/session", this::logout);

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private void addUser(Context ctx) throws DataAccessException {
        var user = new Gson().fromJson(ctx.body(), UserData.class);
        if (user.username() == null || user.password() == null || user.email() == null) {
            ctx.status(400).result("Error: bad request");
            return;
        }
        var auth = registerService.createUser(user.username(), user.password(), user.email());
        ctx.result(new Gson().toJson(auth));
    }

    private void login(Context ctx) throws DataAccessException {
        var userInfo = new Gson().fromJson(ctx.body(), LoginRequest.class);
        if (userInfo.username() == null || userInfo.password() == null) {
            ctx.status(400).result("Error: bad request");
            return;
        }
        var auth = loginService.loginUser(userInfo.username(), userInfo.password());
        ctx.result(new Gson().toJson(auth));
    }

    private void logout(Context ctx) throws DataAccessException {
        String token = ctx.header("authorization");
        if (token == null || token.isBlank()) {
            ctx.status(400).result("Error: bad request");
            return;
        }
        logoutService.logoutUser(token);
        ctx.status(200).result("");
    }

    public void stop() {
        javalin.stop();
    }
}
