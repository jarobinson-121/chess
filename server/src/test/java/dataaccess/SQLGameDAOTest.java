package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQLGameDAOTest {

    static SQLGameDAO gameDAO;

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        DatabaseManager.configureDatabase();
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
    void updateGamePositive() throws DataAccessException {
        gameDAO.createGame("name");

        GameData newGame = new GameData(1, "newusername", null, "name", new ChessGame());

        gameDAO.updateGame(newGame);

        var result = gameDAO.getGame(1);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.whiteUsername());
        Assertions.assertNull(result.blackUsername());
        Assertions.assertEquals("name", result.gameName());
        Assertions.assertEquals(new ChessGame(), result.game());
    }

    @Test
    void updateGameNegative() throws DataAccessException {
        gameDAO.createGame("name");

        GameData newGame = new GameData(0, "newusername", null, "name", new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(newGame);
        });
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        gameDAO.createGame("name");
        gameDAO.createGame("name1");
        gameDAO.createGame("name2");

        var result = gameDAO.listGames();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    void listGamesNegative() throws DataAccessException {
        var result = gameDAO.listGames();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void clearGames() throws DataAccessException {
        gameDAO.createGame("name");
        gameDAO.createGame("name1");
        gameDAO.createGame("name2");

        gameDAO.clearGames();

        var result = gameDAO.listGames();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }
}