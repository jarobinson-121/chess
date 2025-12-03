package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {

    private static TestUser existingUser;
    private static TestUser newUser;
    private static TestCreateRequest createRequest;
    private static Server server;


    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    final LoginService loginService = new LoginService(authDAO, userDAO);
    final LogoutService logoutService = new LogoutService(authDAO);
    final RegisterService registerService = new RegisterService(authDAO, userDAO);

    @BeforeAll
    public static void init() {
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
    }

    @Test
    @DisplayName("Valid Logout")
    void logoutSuccess() throws ResponseException, DataAccessException {
        registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());
        var loginResponse = loginService.loginUser(existingUser.getUsername(), existingUser.getPassword());

        String token = loginResponse.authToken();

        logoutService.logoutUser(token);

        Assertions.assertNull(authDAO.getAuth(token));
    }

    @Test
    @DisplayName("Bad Token")
    void logoutFail() throws ResponseException, DataAccessException {
        registerService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());
        loginService.loginUser(existingUser.getUsername(), existingUser.getPassword());

        Assertions.assertThrows(ResponseException.class, () -> {
            logoutService.logoutUser("1");
        });
    }
}