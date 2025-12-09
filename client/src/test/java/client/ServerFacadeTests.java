package client;

import exception.ResponseException;
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
        var port = server.run(8080);
        facade = new ServerFacade(8080);
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
        var authdata = facade.register("username", "password", "email");

        Assertions.assertNotNull(authdata.authToken());
        Assertions.assertTrue(authdata.authToken().length() > 10);
        Assertions.assertNotEquals(authdata.authToken(), "FailedToke");
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

        Assertions.assertNotNull(loginResult.authToken());
        Assertions.assertTrue(loginResult.authToken().length() > 10);
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
