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

    @BeforeEach
    void clearServer() throws ResponseException {
        facade.clearDB();
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

    @Test
    public void createGamePositive() throws ResponseException {
        var user = facade.registerUser("createtester", "createpassword", "email");

        var response = facade.createGame(user.authToken(), "name");

        assertNotNull(response);
        System.out.println(response.gameID());
        assertTrue(response.gameID() > 0);
    }

    @Test
    public void createGameNegativeNoName() throws ResponseException {
        var user = facade.registerUser("createfailtester", "createfailpassword", "email");

        assertThrows(ResponseException.class, () -> facade.createGame(user.authToken(), null));
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        var user = facade.registerUser("listtester", "listtesterpassword", "email");

        facade.createGame(user.authToken(), "game1");
        facade.createGame(user.authToken(), "game2");
        facade.createGame(user.authToken(), "game3");
        facade.createGame(user.authToken(), "game1");

        var response = facade.listGames(user.authToken());

        assertNotNull(response);
        assertEquals(4, response.games().size());
        assertEquals("game1", response.games().get(0).gameName());
    }

    @Test
    public void listGamesNegativeBadToken() throws ResponseException {
        var user = facade.registerUser("listbadtester", "listbadtesterpassword", "email");

        facade.createGame(user.authToken(), "game1");
        facade.createGame(user.authToken(), "game2");
        facade.createGame(user.authToken(), "game3");
        facade.createGame(user.authToken(), "game1");

        assertThrows(ResponseException.class, () -> facade.listGames("bad-token"));
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        var user = facade.registerUser("jointester", "joinpassword", "email");

        var game = facade.createGame(user.authToken(), "joingame");

        facade.joinGame(user.authToken(), game.gameID(), "WHITE");

        var list = facade.listGames(user.authToken()).games();

        assertNotNull(list);
        assertEquals("jointester", list.get(0).whiteUsername());
    }

    @Test
    public void joinGameNegativeTaken() throws ResponseException {
        var user = facade.registerUser("joinbadtester", "joinbadpass", "email");
        var otherUser = facade.registerUser("tookit", "tookitpass", "email");

        var game = facade.createGame(user.authToken(), "canttouchthis");

        facade.joinGame(otherUser.authToken(), game.gameID(), "WHITE");

        assertThrows(ResponseException.class, () -> facade.joinGame(user.authToken(), game.gameID(), "WHITE"));
    }

    @Test
    public void clearDbTestPositive() throws ResponseException {
        var user1 = facade.registerUser("user1", "password", "email");
        facade.registerUser("user2", "password", "email");
        facade.registerUser("user3", "password", "email");

        facade.createGame(user1.authToken(), "clearGame");
        facade.createGame(user1.authToken(), "clearGame");
        facade.createGame(user1.authToken(), "clearGame");

        facade.clearDB();

        assertThrows(Exception.class, () -> facade.createGame(user1.authToken()));
    }

    @Test
    public void logoutPositive() throws ResponseException {
        var user = facade.registerUser("logoutuser", "logoutpassword", "email");

        facade.logout(user.authToken());

        assertThrows(ResponseException.class, () -> facade.listGames(user.authToken()));
    }

    @Test
    public void logoutNegativeBadToken() throws ResponseException {
        facade.registerUser("logoutbaduser", "logoutbadpassword", "email");

        assertThrows(ResponseException.class, () -> facade.logout("bad-token"));
    }
}
