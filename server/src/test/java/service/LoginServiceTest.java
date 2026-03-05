package service;

import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryUserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    private static UserData testUser;

    static final MemoryAuthDao authDao = new MemoryAuthDao();
    static final MemoryUserDao userDao = new MemoryUserDao();
    static final RegisterService registerService = new RegisterService(authDao, userDao);
    static final LoginService loginService = new LoginService(authDao, userDao);

    @BeforeAll
    public static void init() throws ResponseException {
        testUser = new UserData("newUser", "newUserPassword", "eu@mail.com");
        registerService.createUser(testUser);
    }

    @Test
    void LoginUserSuccess() throws ResponseException {
        AuthData auth = loginService.loginUser(testUser.username(), testUser.password());

        assertEquals(auth.username(), testUser.username());
        assertNotNull(auth.authToken());
    }

    @Test
    void LoginUserFailBadPassword() {
        assertThrows(ResponseException.class, () -> {
            loginService.loginUser(testUser.username(), "BadPass");
        });
    }

}
