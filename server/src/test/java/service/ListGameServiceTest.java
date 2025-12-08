package service;

import dataaccess.DataAccessException;
import dataaccess.memorydaos.MemoryAuthDAO;
import dataaccess.memorydaos.MemoryGameDAO;
import dataaccess.memorydaos.MemoryUserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.model.TestUser;

class ListGameServiceTest {

    private static TestUser existingUser;

    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    final RegisterService registerService = new RegisterService(authDAO, userDAO);
    final CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
    final ListGameService listGameService = new ListGameService(authDAO, gameDAO);

    @BeforeAll
    public static void init() {
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

    }

    @Test
    @DisplayName("Valid empty")
    void listGameSuccessEmpty() throws ResponseException, DataAccessException {
        var regResponse = registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        var result = listGameService.listGames(regResponse.authToken());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(!result.isEmpty());
    }

    @Test
    @DisplayName("Valid list")
    void listGameSuccess() throws ResponseException, DataAccessException {
        var regResponse = registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        createGameService.createGame(regResponse.authToken(), "Valid Name");

        var result = listGameService.listGames(regResponse.authToken());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(!result.isEmpty());
    }

    @Test
    @DisplayName("Bad Token")
    void listGamesBadToken() throws ResponseException, DataAccessException {
        var regResponse = registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        createGameService.createGame(regResponse.authToken(), "Valid Name");

        Assertions.assertThrows(ResponseException.class, () -> {
            listGameService.listGames("bad-token");
            ;
        });
    }

}