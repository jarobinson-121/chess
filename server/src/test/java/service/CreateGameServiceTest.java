package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameServiceTest {

    private static TestUser existingUser;
    private static TestUser newUser;
    private static TestCreateRequest createRequest;
    private static Server server;


    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    final RegisterService registerService = new RegisterService(authDAO, userDAO);
    final CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);

    @BeforeAll
    public static void init() {
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
    }

    @Test
    @DisplayName("Valid Create")
    void createGameSuccess() throws ResponseException, DataAccessException {
        var regResponse = registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        var result = createGameService.createGame(regResponse.authToken(), "Valid Name");

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.gameName());
        Assertions.assertNotNull(result.gameID());
        Assertions.assertEquals(1, result.gameID());
    }

    @Test
    @DisplayName("Bad Token")
    void createGameFailToken() throws ResponseException {
        registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        Assertions.assertThrows(ResponseException.class, () -> {
            createGameService.createGame("bad-token", "Valid Name");
        });
    }
}