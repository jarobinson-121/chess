package service;

import dataaccess.DataAccessException;
import dataaccess.memorydaos.MemoryAuthDAO;
import dataaccess.memorydaos.MemoryUserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.model.TestUser;


class RegisterServiceTest {

    private static TestUser newUser;

    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    final RegisterService registerService = new RegisterService(authDAO, userDAO);

    @BeforeAll
    public static void init() {
        newUser = new TestUser("newUser", "newUserPassword", "eu@mail.com");
    }

    @Test
    @DisplayName("Valid Register")
    void registerSuccess() throws ResponseException, DataAccessException {
        var result = registerService.createUser(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals(newUser.getUsername(), result.username());
    }

    @Test
    @DisplayName("Taken Username")
    void registerFailUsername() throws ResponseException {
        registerService.createUser(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());

        Assertions.assertThrows(ResponseException.class, () -> {
            registerService.createUser(newUser.getUsername(), "Password", null);
        });
    }

}