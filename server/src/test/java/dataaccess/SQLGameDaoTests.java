package dataaccess;

import chess.ChessGame;
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
    void updateGamePositive() throws DataAccessException {
        gameDao.createGame("name");

        GameData newGame = new GameData(1, "newusername", null, "name", new ChessGame());

        gameDao.updateGame(newGame);

        var result = gameDao.getGame(1);

        assertNotNull(result);
        assertNotNull(result.whiteUsername());
        assertNull(result.blackUsername());
        assertEquals("name", result.gameName());
        assertEquals(new ChessGame(), result.game());
    }

    @Test
    void updateGameNegative() throws DataAccessException {
        gameDao.createGame("name");

        GameData newGame = new GameData(0, "newusername", null, "name", new ChessGame());

        assertThrows(DataAccessException.class, () -> {
            gameDao.updateGame(newGame);
        });
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        gameDao.createGame("name");
        gameDao.createGame("name1");
        gameDao.createGame("name2");

        var result = gameDao.listGames();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void listGamesNegative() throws DataAccessException {
        var result = gameDao.listGames();

        assertNotNull(result);
        assertEquals(0, result.size());
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
