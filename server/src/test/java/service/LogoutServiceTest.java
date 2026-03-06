package service;

import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryGameDao;
import dataaccess.memory.MemoryUserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {
    private static UserData testUser;
    private static AuthData auth;

    static final MemoryAuthDao authDao = new MemoryAuthDao();
    static final MemoryUserDao userDao = new MemoryUserDao();
    static final RegisterService registerService = new RegisterService(authDao, userDao);
    static final LoginService loginService = new LoginService(authDao, userDao);
    static final LogoutService logoutService = new LogoutService(authDao);

    @BeforeAll
    public static void init() throws ResponseException {
        testUser = new UserData("newUser", "newUserPassword", "eu@mail.com");
        registerService.createUser(testUser);
        auth = loginService.loginUser(testUser.username(), testUser.password());
    }

    @Test
    void LogoutSuccess() throws ResponseException {
        logoutService.logoutUser(auth.authToken());

        assertThrows(ResponseException.class, () -> {
            logoutService.logoutUser(auth.authToken());
        });
    }

    @Test
    void LogoutFailBadToken() {
        assertThrows(ResponseException.class, () -> {
            logoutService.logoutUser("BadPass");
        });
    }
}
