package service;

import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryUserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    private static UserData testUser;

    static final MemoryAuthDao AUTH_DAO = new MemoryAuthDao();
    static final MemoryUserDao USER_DAO = new MemoryUserDao();
    static final RegisterService REGISTER_SERVICE = new RegisterService(AUTH_DAO, USER_DAO);
    static final LoginService LOGIN_SERVICE = new LoginService(AUTH_DAO, USER_DAO);

    @BeforeAll
    public static void init() throws ResponseException {
        testUser = new UserData("newUser", "newUserPassword", "eu@mail.com");
        REGISTER_SERVICE.createUser(testUser);
    }

    @Test
    void loginUserSuccess() throws ResponseException {
        AuthData auth = LOGIN_SERVICE.loginUser(testUser.username(), testUser.password());

        assertEquals(auth.username(), testUser.username());
        assertNotNull(auth.authToken());
    }

    @Test
    void loginUserFailBadPassword() {
        assertThrows(ResponseException.class, () -> {
            LOGIN_SERVICE.loginUser(testUser.username(), "BadPass");
        });
    }

}
