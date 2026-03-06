package service;

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

class CreateGameServiceTest {

    private static UserData testUser;
    private static AuthData auth;

    static final MemoryAuthDao AUTH_DAO = new MemoryAuthDao();
    static final MemoryUserDao USER_DAO = new MemoryUserDao();
    static final MemoryGameDao GAME_DAO = new MemoryGameDao();
    static final RegisterService REGISTER_SERVICE = new RegisterService(AUTH_DAO, USER_DAO);
    static final LoginService LOGIN_SERVICE = new LoginService(AUTH_DAO, USER_DAO);
    static final CreateGameService CREATE_GAME_SERVICE = new CreateGameService(AUTH_DAO, GAME_DAO);

    @BeforeAll
    public static void init() throws ResponseException {
        testUser = new UserData("newUser", "newUserPassword", "eu@mail.com");
        REGISTER_SERVICE.createUser(testUser);
        auth = LOGIN_SERVICE.loginUser(testUser.username(), testUser.password());
    }

    @Test
    void createGameSuccess() throws ResponseException {
        GameData game = CREATE_GAME_SERVICE.createGame(auth.authToken(), "gameName");

        assertNotNull(game);
        assertEquals(game.gameID(), 1);
    }

    @Test
    void createGameFailBadToken() {
        assertThrows(ResponseException.class, () -> {
            CREATE_GAME_SERVICE.createGame("badToken", "gameName");
        });
    }
}
