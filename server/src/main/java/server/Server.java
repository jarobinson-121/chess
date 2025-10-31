package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.Request;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.RegisterService;
import model.UserData;
import org.eclipse.jetty.server.Response;

public class Server {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final RegisterService registerService;

    private final Javalin javalin;

    public Server() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        registerService = new RegisterService(authDAO, userDAO);
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::addUser);


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

    public void stop() {
        javalin.stop();
    }
}
