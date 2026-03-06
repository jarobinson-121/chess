package service;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryGameDao;
import dataaccess.memory.MemoryUserDao;
import exception.ResponseException;
import models.AuthData;
import models.GameData;
import models.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameServiceTest {

    private static UserData testUser;
    private static UserData fillerUser1;
    private static UserData fillerUser2;
    private static AuthData auth;
    private static AuthData filler1;
    private static AuthData filler2;
    private static GameData game;

    static final MemoryAuthDao authDao = new MemoryAuthDao();
    static final MemoryUserDao userDao = new MemoryUserDao();
    static final MemoryGameDao gameDao = new MemoryGameDao();
    static final RegisterService registerService = new RegisterService(authDao, userDao);
    static final LoginService loginService = new LoginService(authDao, userDao);
    static final CreateGameService createGameService = new CreateGameService(authDao, gameDao);
    static final JoinGameService joinGameService = new JoinGameService(authDao, gameDao);

    @BeforeAll
    public static void init() throws ResponseException {
        testUser = new UserData("newUser", "newUserPassword", "eu@mail.com");
        fillerUser1 = new UserData("otherUser1", "password", "");
        fillerUser2 = new UserData("otherUser2", "otherPassword", "");
        registerService.createUser(testUser);
        registerService.createUser(fillerUser1);
        registerService.createUser(fillerUser2);
        auth = loginService.loginUser(testUser.username(), testUser.password());
        filler1 = loginService.loginUser(fillerUser1.username(), fillerUser1.password());
        filler2 = loginService.loginUser(fillerUser2.username(), fillerUser2.password());
        game = createGameService.createGame(auth.authToken(), "gameName");
    }

    @Test
    void JoinGameSuccess() throws ResponseException, DataAccessException {
        joinGameService.JoinGame(auth.authToken(), "white", 1);

        GameData gameChecker = gameDao.getGame(1);

        assertNotNull(game);
        assertNotNull(gameChecker);
        assertEquals(gameChecker.gameID(), 1);
        assertEquals(gameChecker.whiteUsername(), testUser.username());
    }

    @Test
    void JoinGameFailFull() throws ResponseException {
        createGameService.createGame(filler1.authToken(), "otherGame");
        joinGameService.JoinGame(filler1.authToken(), "black", 2);
        joinGameService.JoinGame(filler2.authToken(), "white", 2);

        assertThrows(ResponseException.class, () -> {
            joinGameService.JoinGame(auth.authToken(), "white", 2);
        });
    }
}
