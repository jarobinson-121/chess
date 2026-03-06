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

public class ListGamesServiceTest {
    private static UserData testUser;
    private static AuthData auth;
    private static GameData game;

    static final MemoryAuthDao authDao = new MemoryAuthDao();
    static final MemoryUserDao userDao = new MemoryUserDao();
    static final MemoryGameDao gameDao = new MemoryGameDao();
    static final RegisterService registerService = new RegisterService(authDao, userDao);
    static final LoginService loginService = new LoginService(authDao, userDao);
    static final CreateGameService createGameService = new CreateGameService(authDao, gameDao);
    static final JoinGameService joinGameService = new JoinGameService(authDao, gameDao);
    static final ListGamesService listGameService = new ListGamesService(authDao, gameDao);

    @BeforeAll
    public static void init() throws ResponseException {
        testUser = new UserData("newUser", "newUserPassword", "eu@mail.com");
        registerService.createUser(testUser);
        auth = loginService.loginUser(testUser.username(), testUser.password());
    }

    @Test
    void EmptyListGamesSuccess() throws ResponseException, DataAccessException {
        gameDao.clearGames();

        var list = listGameService.listGames(auth.authToken());

        assertNotNull(list);
    }

    @Test
    void List10GamesSuccess() throws ResponseException {
        for (int i = 0; i < 10; i++) {
            createGameService.createGame(auth.authToken(), "TitlesCanRepeat");
        }

        var list = listGameService.listGames(auth.authToken());

        assertNotNull(list);
    }


    @Test
    void ListGamesFailToken() {

        assertThrows(ResponseException.class, () -> {
            listGameService.listGames("badToken");
        });
    }
}
