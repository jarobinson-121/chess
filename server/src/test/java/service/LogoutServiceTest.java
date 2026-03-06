package service;

import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryUserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {
    private static UserData testUser;
    private static AuthData auth;

    static final MemoryAuthDao AUTH_DAO = new MemoryAuthDao();
    static final MemoryUserDao USER_DAO = new MemoryUserDao();
    static final RegisterService REGISTER_SERVICE = new RegisterService(AUTH_DAO, USER_DAO);
    static final LoginService LOGIN_SERVICE = new LoginService(AUTH_DAO, USER_DAO);
    static final LogoutService LOGOUT_SERVICE = new LogoutService(AUTH_DAO);

    @BeforeAll
    public static void init() throws ResponseException {
        testUser = new UserData("newUser", "newUserPassword", "eu@mail.com");
        REGISTER_SERVICE.createUser(testUser);
        auth = LOGIN_SERVICE.loginUser(testUser.username(), testUser.password());
    }

    @Test
    void logoutSuccess() throws ResponseException {
        LOGOUT_SERVICE.logoutUser(auth.authToken());

        assertThrows(ResponseException.class, () -> {
            LOGOUT_SERVICE.logoutUser(auth.authToken());
        });
    }

    @Test
    void logoutFailBadToken() {
        assertThrows(ResponseException.class, () -> {
            LOGOUT_SERVICE.logoutUser("BadPass");
        });
    }
}
