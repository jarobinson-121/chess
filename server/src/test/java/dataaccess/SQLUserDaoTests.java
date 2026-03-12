package dataaccess;

import dataaccess.sql.SQLUserDao;
import exception.ResponseException;
import models.UserData;
import org.junit.jupiter.api.*;

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
}
