package dataaccess;

import dataaccess.sql.SQLAuthDao;
import exception.ResponseException;
import models.AuthData;
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

    }
}
