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
    private static AuthData auth;
    private static AuthData filler1;
    private static GameData game;

    static final MemoryAuthDao AUTH_DAO = new MemoryAuthDao();
    static final MemoryUserDao USER_DAO = new MemoryUserDao();
    static final MemoryGameDao GAME_DAO = new MemoryGameDao();
    static final RegisterService REGISTER_SERVICE = new RegisterService(AUTH_DAO, USER_DAO);
    static final LoginService LOGIN_SERVICE = new LoginService(AUTH_DAO, USER_DAO);
    static final CreateGameService CREATE_GAME_SERVICE = new CreateGameService(AUTH_DAO, GAME_DAO);
    static final ClearDBService CLEAR_DB_SERVICE = new ClearDBService(AUTH_DAO, GAME_DAO, USER_DAO);

    @BeforeAll
    public static void init() throws ResponseException {
        testUser = new UserData("newUser", "newUserPassword", "eu@mail.com");
        fillerUser1 = new UserData("otherUser1", "password", "");
        REGISTER_SERVICE.createUser(testUser);
        REGISTER_SERVICE.createUser(fillerUser1);
        auth = LOGIN_SERVICE.loginUser(testUser.username(), testUser.password());
        filler1 = LOGIN_SERVICE.loginUser(fillerUser1.username(), fillerUser1.password());
        game = CREATE_GAME_SERVICE.createGame(auth.authToken(), "gameName");
    }

    @Test
    void clearDBSuccess() throws ResponseException, DataAccessException {
        CLEAR_DB_SERVICE.clearDB();

        assertDoesNotThrow(() -> CLEAR_DB_SERVICE.clearDB());

        assertNull(USER_DAO.getUser(testUser.username()));
        assertNull(USER_DAO.getUser(filler1.username()));
    }
}
