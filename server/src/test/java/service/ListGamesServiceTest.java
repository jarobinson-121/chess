package service;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryGameDao;
import dataaccess.memory.MemoryUserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesServiceTest {
    private static UserData testUser;
    private static AuthData auth;


    static final MemoryAuthDao AUTH_DAO = new MemoryAuthDao();
    static final MemoryUserDao USER_DAO = new MemoryUserDao();
    static final MemoryGameDao GAME_DAO = new MemoryGameDao();
    static final RegisterService REGISTER_SERVICE = new RegisterService(AUTH_DAO, USER_DAO);
    static final LoginService LOGIN_SERVICE = new LoginService(AUTH_DAO, USER_DAO);
    static final CreateGameService CREATE_GAME_SERVICE = new CreateGameService(AUTH_DAO, GAME_DAO);
    static final ListGamesService LIST_GAMES_SERVICE = new ListGamesService(AUTH_DAO, GAME_DAO);

    @BeforeAll
    public static void init() throws ResponseException {
        testUser = new UserData("newUser", "newUserPassword", "eu@mail.com");
        REGISTER_SERVICE.createUser(testUser);
        auth = LOGIN_SERVICE.loginUser(testUser.username(), testUser.password());
    }

    @Test
    void emptyListGamesSuccess() throws ResponseException, DataAccessException {
        GAME_DAO.clearGames();

        var list = LIST_GAMES_SERVICE.listGames(auth.authToken());

        assertNotNull(list);
    }

    @Test
    void list10GamesSuccess() throws ResponseException {
        for (int i = 0; i < 10; i++) {
            CREATE_GAME_SERVICE.createGame(auth.authToken(), "TitlesCanRepeat");
        }

        var list = LIST_GAMES_SERVICE.listGames(auth.authToken());

        assertNotNull(list);
    }


    @Test
    void listGamesFailToken() {

        assertThrows(ResponseException.class, () -> {
            LIST_GAMES_SERVICE.listGames("badToken");
        });
    }
}
