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
            gameDAO.createGame("");
        });
    }

    @Test
    void getGame() {
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