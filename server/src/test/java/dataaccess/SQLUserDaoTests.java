package dataaccess;

import dataaccess.sql.SQLUserDao;
import exception.ResponseException;
import models.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDaoTests {

    static SQLUserDao userDao;
    UserData user = new UserData("username", "password", "email@website.com");

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        DatabaseManager.configureDatabase();
        userDao = new SQLUserDao();
    }

    @BeforeEach
    void emptyUserDB() throws DataAccessException, ResponseException {
        userDao.clearUsers();
    }

    @Test
    void addRetrieveUserPositive() throws DataAccessException {
        userDao.createUser(user);

        UserData retrieved = userDao.getUser(user.username());
        assertNotNull(retrieved);
        assertEquals(retrieved.username(), user.username());
    }

    @Test
    void addUserFailDuplicateUsername() throws DataAccessException {
        userDao.createUser(user);

        Assertions.assertThrows(DataAccessException.class, () -> {
            userDao.createUser(user);
        });
    }

    @Test
    void getUserSuccess() throws DataAccessException {
        UserData tester = userDao.createUser(user);

        UserData retrieved = userDao.getUser(tester.username());


        assertNotNull(retrieved);
        assertEquals(user.username(), retrieved.username());
        assertTrue(BCrypt.checkpw("password", retrieved.password()));
    }

    @Test
    void getUserFailMissingUser() throws DataAccessException {
        UserData retrieved = userDao.getUser("badUname");

        assertNull(retrieved);
    }

    @Test
    void clearUsersSuccess() throws DataAccessException {
        userDao.createUser(new UserData("username0", "password", "email@website"));
        userDao.createUser(new UserData("username1", "password", "email@website"));
        userDao.createUser(new UserData("username2", "password", "email@website"));

        userDao.clearUsers();

        Assertions.assertNull(userDao.getUser("username0"));
        Assertions.assertNull(userDao.getUser("username1"));
        Assertions.assertNull(userDao.getUser("username2"));
    }
}
