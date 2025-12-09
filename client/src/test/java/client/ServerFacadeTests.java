package client;

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

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerPositive() throws URISyntaxException, IOException, InterruptedException {
        var authdata = facade.register("username", "password", "email");

        Assertions.assertNotNull(authdata.authToken());
        Assertions.assertTrue(authdata.authToken().length() > 10);
        Assertions.assertNotEquals(authdata.authToken(), "FailedToke");
    }

    // need to write the clear


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
