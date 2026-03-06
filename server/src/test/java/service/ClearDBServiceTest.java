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

public class ClearDBServiceTest {

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
    static final ClearDBService clearDBService = new ClearDBService(authDao, gameDao, userDao);

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
    void ClearDBSuccess() throws ResponseException, DataAccessException {
        clearDBService.clearDB();

        assertDoesNotThrow(() -> clearDBService.clearDB());

        assertNull(userDao.getUser(testUser.username()));
        assertNull(userDao.getUser(filler1.username()));
    }
}
