package dataaccess;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    static SQLAuthDAO authDAO;

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        SQLInitializer.configureDatabase();
        authDAO = new SQLAuthDAO();
    }

    @BeforeEach
    void emptyAuthDB() throws DataAccessException {
        authDAO.clearAuths();
    }

    @Test
    void createAuthPositive() throws DataAccessException {
        var result = authDAO.createAuth("token", "username");

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals("username", result.username());
        Assertions.assertEquals("token", result.authToken());
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
        var result = authDAO.createAuth("token", "username");

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals("username", result.username());
        Assertions.assertEquals("token", result.authToken());
    }

    @Test
    void getAuthNegative() throws DataAccessException {
        authDAO.createAuth("token", "username");

        var result = authDAO.getAuth("badToken");

        Assertions.assertNull(result);
    }

    @Test
    void deleteAuth() {
    }

    @Test
    void clearAuths() {
    }
}