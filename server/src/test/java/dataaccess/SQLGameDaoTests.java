package dataaccess;

import dataaccess.sql.SQLGameDao;
import exception.ResponseException;
import models.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDaoTests {

    static SQLGameDao gameDao;

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        DatabaseManager.configureDatabase();
        gameDao = new SQLGameDao();
    }

    @BeforeEach
    void clearGamesDb() throws DataAccessException {
        gameDao.clearGames();
    }

    @Test
    void addRetrieveGameSuccess() throws DataAccessException {
        GameData tester = gameDao.createGame("gameName");

        GameData retrieved = gameDao.getGame(tester.gameID());

        assertNotNull(retrieved);
        assertNotNull(tester);
        assertEquals(retrieved.gameName(), tester.gameName());
    }

    @Test
    void addGameFailBadId() throws DataAccessException {
        gameDao.createGame("name");

        assertNull(gameDao.getGame(0));
    }

    @Test
    void getGamePositive() throws DataAccessException {
        gameDao.createGame("name");

        var result = gameDao.getGame(1);

        assertNotNull(result);
        assertEquals(1, result.gameID());
        assertEquals("name", result.gameName());
        assertNull(result.blackUsername());
        assertNull(result.whiteUsername());
    }

    @Test
    void getGameNegative() throws DataAccessException {
        gameDao.createGame("name");

        assertNull(gameDao.getGame(0));
    }

    @Test
    void clearGamesSuccess() throws DataAccessException {
        for (int i = 0; i < 5; i++) {
            gameDao.createGame("name");
        }

        assertNotNull(gameDao.getGame(1));
        assertNotNull(gameDao.getGame(5));

        gameDao.clearGames();

        assertNull(gameDao.getGame(1));
    }
}
