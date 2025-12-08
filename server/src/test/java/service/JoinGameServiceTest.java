package service;

import dataaccess.DataAccessException;
import dataaccess.memorydaos.MemoryAuthDAO;
import dataaccess.memorydaos.MemoryGameDAO;
import dataaccess.memorydaos.MemoryUserDAO;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.model.TestUser;

class JoinGameServiceTest {

    private static TestUser existingUser;

    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    final RegisterService registerService = new RegisterService(authDAO, userDAO);
    final CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
    final JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);

    @BeforeAll
    public static void init() {
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

    }

    @Test
    @DisplayName("Valid Join")
    void joinGameSuccess() throws ResponseException, DataAccessException {
        var regResponse = registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        var createResponse = createGameService.createGame(regResponse.authToken(), "Valid Name");

        joinGameService.joinGame(regResponse.authToken(), "BLACK", createResponse.gameID());

        GameData game = gameDAO.getGame(createResponse.gameID());

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(game.blackUsername());
        Assertions.assertEquals(existingUser.getUsername(), game.blackUsername());
    }

    @Test
    @DisplayName("Bad Color")
    void joinGameBadColor() throws ResponseException, DataAccessException {
        var regResponse = registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        var createResponse = createGameService.createGame(regResponse.authToken(), "Valid Name");

        Assertions.assertThrows(ResponseException.class, () -> {
            joinGameService.joinGame(regResponse.authToken(), "GREEN", createResponse.gameID());
            ;
        });
    }

}