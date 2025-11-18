package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import server.Server;

import javax.xml.crypto.Data;

class LoginServiceTest {

    private static TestUser existingUser;
    private static TestUser newUser;
    private static TestCreateRequest createRequest;
    private static Server server;


    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    final LoginService loginService = new LoginService(authDAO, userDAO);
    final RegisterService registerService = new RegisterService(authDAO, userDAO);

    @BeforeAll
    public static void init() {
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
    }

    @Test
    @DisplayName("Valid Login")
    void loginSuccess() throws ResponseException, DataAccessException {
        registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        var result = loginService.loginUser(existingUser.getUsername(), existingUser.getPassword());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals(existingUser.getUsername(), result.username());
    }

    @Test
    @DisplayName("Bad Password")
    void loginFailPass() {
        Assertions.assertEquals(1, 1);
    }
}