package service;

import dataaccess.memory.MemoryAuthDao;
import dataaccess.memory.MemoryUserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    static final RegisterService REGISTER_SERVICE = new RegisterService(new MemoryAuthDao(), new MemoryUserDao());


    @Test
    void addUserSuccess() throws ResponseException {
        UserData testUser = new UserData("testUsername", "password", "email");
        AuthData auth = REGISTER_SERVICE.createUser(testUser);

        assertEquals(auth.username(), testUser.username());
        assertNotNull(auth.authToken());
    }

    @Test
    void addUserFailTaken() throws ResponseException {
        UserData testUser = new UserData("takenUsername", "password", "email");
        REGISTER_SERVICE.createUser(testUser);

        assertThrows(ResponseException.class, () -> {
            REGISTER_SERVICE.createUser(testUser);
        });
    }
}
