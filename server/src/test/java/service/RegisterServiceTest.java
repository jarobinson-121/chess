package service;

import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryUserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    static final RegisterService registerService = new RegisterService(new MemoryAuthDao(), new MemoryUserDao());


    @Test
    void addUserSuccess() throws ResponseException {
        UserData testUser = new UserData("testUsername", "password", "email");
        AuthData auth = registerService.createUser(testUser);

        assertEquals(auth.username(), testUser.username());
        assertNotNull(auth.authToken());
    }

    @Test
    void addUserFailTaken() throws ResponseException {
        UserData testUser = new UserData("takenUsername", "password", "email");
        registerService.createUser(testUser);

        assertThrows(ResponseException.class, () -> {
            registerService.createUser(testUser);
        });
    }
}
