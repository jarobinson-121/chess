package client;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(9000);
        facade = new ServerFacade(9000);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void clearDb() throws URISyntaxException, IOException, InterruptedException {
        facade.clearDB();
    }

    @AfterAll
    static void stopServer() throws URISyntaxException, IOException, InterruptedException {
        facade.clearDB();
        server.stop();
    }

    @Test
    public void registerPositive() throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ResponseException {
        var result = facade.register("username", "password", "email");
        AuthData authData = new Gson().fromJson(result, AuthData.class);

        Assertions.assertNotNull(authData.authToken());
        Assertions.assertTrue(authData.authToken().length() > 10);
        Assertions.assertNotEquals(authData.authToken(), "FailedToke");
    }

    @Test
    public void registerNegative() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.register("username", "password");
        });
    }

    @Test
    public void loginPositive() throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ResponseException {
        facade.register("username", "password", "email");
        var loginResult = facade.login("username", "password");
        AuthData authData = new Gson().fromJson(loginResult, AuthData.class);

        Assertions.assertNotNull(authData.authToken());
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void loginNegative() throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ResponseException {
        facade.register("username", "password", "email");

        Assertions.assertThrows(ResponseException.class, () -> {
            facade.login("username", "badPassword");
        });
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
