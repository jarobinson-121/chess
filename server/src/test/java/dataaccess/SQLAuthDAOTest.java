package dataaccess;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQLAuthDAOTest {

    static SQLAuthDAO authDAO;

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        DatabaseManager.configureDatabase();
        authDAO = new SQLAuthDAO();
    }

    @BeforeEach
    void emptyAuthDB() throws DataAccessException {
        authDAO.clearAuths();
    }

    private void createGetAssertions() throws DataAccessException {
        var result = authDAO.createAuth("token", "username");

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals("username", result.username());
        Assertions.assertEquals("token", result.authToken());
    }

    @Test
    void createAuthPositive() throws DataAccessException {
        createGetAssertions();
    }

    @Test
    void createAuthNegative() throws DataAccessException {
        AuthData auth = new AuthData("token1", "username");
        AuthData otherAuth = new AuthData("token1", "otheruser");

        authDAO.createAuth(auth.authToken(), auth.username());

        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(otherAuth.authToken(), otherAuth.username());
        });
    }

    @Test
    void getAuthPositive() throws DataAccessException {
        createGetAssertions();
    }

    @Test
    void getAuthNegative() throws DataAccessException {
        authDAO.createAuth("token", "username");

        var result = authDAO.getAuth("badToken");

        Assertions.assertNull(result);
    }

    @Test
    void deleteAuthPositive() throws DataAccessException {
        authDAO.createAuth("token", "username");

        authDAO.deleteAuth("token");

        var result = authDAO.getAuth("token");

        Assertions.assertNull(result);
    }

    @Test
    void clearAuths() throws DataAccessException {
        authDAO.createAuth("token", "username");
        authDAO.createAuth("token1", "username1");
        authDAO.createAuth("token2", "username2");
        authDAO.createAuth("token3", "username3");

        authDAO.clearAuths();

        Assertions.assertNull(authDAO.getAuth("token"));
        Assertions.assertNull(authDAO.getAuth("token1"));
        Assertions.assertNull(authDAO.getAuth("token2"));
        Assertions.assertNull(authDAO.getAuth("token3"));
    }
}