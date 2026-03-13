package dataaccess;

import dataaccess.sql.SQLAuthDao;
import exception.ResponseException;
import models.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void emptyAuthDB() throws DataAccessException, ResponseException {
        authDao.clearAuths();
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
    void addFailNoUserame() throws DataAccessException {
        authDao.createAuth(auth.username());

        assertThrows(DataAccessException.class, () -> {
            authDao.createAuth(null);
        });
    }

    @Test
    void getAuthFailBadToken() throws DataAccessException {
        authDao.createAuth(auth.username());

        assertNull(authDao.getAuth(auth.authToken()));
    }

    @Test
    void deleteAuthSuccess() throws DataAccessException {
        AuthData tester = authDao.createAuth("TestName");
        AuthData retrieved = authDao.getAuth(tester.authToken());
        authDao.deleteAuth(retrieved.authToken());

        assertNull(authDao.getAuth(retrieved.authToken()));
    }

    @Test
    void deleteAuthFailBadToken() throws DataAccessException {
        AuthData tester = authDao.createAuth("TestName");
        authDao.deleteAuth("wrongToken");
        AuthData retrieved = authDao.getAuth(tester.authToken());

        assertNotNull(retrieved);
        assertEquals(tester.username(), retrieved.username());
    }

    @Test
    void clearAuthsSuccess() throws DataAccessException {
        AuthData one = authDao.createAuth("username1");
        AuthData two = authDao.createAuth("username2");
        AuthData three = authDao.createAuth("username3");

        authDao.clearAuths();

        assertNull(authDao.getAuth(one.authToken()));
        assertNull(authDao.getAuth(two.authToken()));
        assertNull(authDao.getAuth(three.authToken()));
    }
}
