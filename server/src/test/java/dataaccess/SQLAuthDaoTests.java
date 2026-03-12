package dataaccess;

import dataaccess.sql.SQLAuthDao;
import dataaccess.sql.SQLUserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDaoTests {

    static SQLAuthDao authDao;
    AuthData auth = new AuthData("token", "username");

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        DatabaseManager.configureDatabase();
        authDao = new SQLAuthDao();
    }

    @Test
    void addRetrieveAuthPositive() throws DataAccessException {
        AuthData tester = authDao.createAuth(auth.username());

        AuthData retrieved = authDao.getAuth(tester.authToken());
        assertNotNull(retrieved);
        assertEquals(retrieved.username(), auth.username());
        assertEquals(retrieved.authToken(), retrieved.authToken());
    }

    @Test
    void getUserFailBadToken() throws DataAccessException {
        AuthData tester = authDao.createAuth(auth.username());

        assertNull(authDao.getAuth(auth.authToken()));
    }
}
