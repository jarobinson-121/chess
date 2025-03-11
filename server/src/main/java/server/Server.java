package server;

import com.google.gson.Gson;
import Service.RegisterService;
import Service.LoginService;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.LoginRequest;
import dataaccess.UserDAO;
import spark.*;
import model.UserData;

public class Server {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final RegisterService registerService;
    private final LoginService loginService;

    public Server(UserDAO userDAO, AuthDAO authDAO, RegisterService registerService, LoginService loginService) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.registerService = registerService;
        this.loginService = loginService;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

//        Spark.delete("/db", this::clearDatabase);
        Spark.post("/user", this::addUser);
        Spark.post("/session", this::login);

//        Spark.awaitInitialization();
//        //This line initializes the server and can be removed once you have a functioning endpoint
//        Spark.init();
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

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}