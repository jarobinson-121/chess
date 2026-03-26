package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var url = String.format("http://localhost:%d", server.run(0));
        System.out.println("Started test HTTP server on " + url);
        facade = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerPositive() throws ResponseException {
        var response = facade.registerUser("testusername", "testpassword", "testemail");

        assertEquals("testusername", response.username());
        assertNotNull(response.authToken());
    }

    @Test
    public void registerNegativeTakenUsername() throws ResponseException {
        facade.registerUser("newuser", "password", "email");

        assertThrows(ResponseException.class, () -> facade.registerUser("newuser", "password", "email"));
    }

    @Test
    public void loginPositive() throws ResponseException {
        facade.registerUser("logintester", "loginpassword");

        var response = facade.loginUser("logintester", "loginpassword");

        assertEquals("logintester", response.username());
        assertNotNull(response.authToken());
    }

    @Test
    public void loginNegativeBadPassword() throws ResponseException {
        facade.registerUser("badlogintester", "badloginpassword", "");

        assertThrows(ResponseException.class, () -> facade.loginUser("badlogintester", "pooppassword"));
    }

}
