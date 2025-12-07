package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {

    static SQLGameDAO gameDAO;

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        SQLInitializer.configureDatabase();
        gameDAO = new SQLGameDAO();
    }

    @BeforeEach
    void emptyDB() throws DataAccessException {
        gameDAO.clearGames();
    }

    @Test
    void createGamePositive() throws DataAccessException {
        var result = gameDAO.createGame("name");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.gameID());
        Assertions.assertEquals("name", result.gameName());
        Assertions.assertNull(result.blackUsername());
        Assertions.assertNull(result.whiteUsername());
    }

    @Test
    void createGameNegative() throws DataAccessException {

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(null);
        });
    }

    @Test
    void getGamePositive() throws DataAccessException {
        gameDAO.createGame("name");

        var result = gameDAO.getGame(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.gameID());
        Assertions.assertEquals("name", result.gameName());
        Assertions.assertNull(result.blackUsername());
        Assertions.assertNull(result.whiteUsername());
    }

    @Test
    void getGameNegative() throws DataAccessException {
        gameDAO.createGame("name");

        Assertions.assertNull(gameDAO.getGame(0));
    }

    @Test
    void updateGame() {
    }

    @Test
    void listGames() {
    }

    @Test
    void clearGames() {
    }
}