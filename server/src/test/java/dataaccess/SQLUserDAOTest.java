package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {

    static SQLUserDAO userDAO;

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        SQLInitializer.configureDatabase();
        userDAO = new SQLUserDAO();
    }

    @Test
    void addUserPositive() throws DataAccessException {
        UserData user = new UserData("username", "password", "email@website");
        userDAO.addUser(user);

        UserData getUser = userDAO.getUserByUsername("username");
        Assertions.assertNotNull(getUser);
        Assertions.assertEquals("username", getUser.username());
        Assertions.assertEquals("password", getUser.password());
        Assertions.assertEquals("email@website", getUser.email());
    }

    @Test
    void addUserUsernameTaken() throws DataAccessException {
        UserData user = new UserData("username", "password", "email@website");
        userDAO.addUser(user);

        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(user);
        });
    }

    @Test
    void getUserByUsername() {
    }

    @Test
    void clearUsers() {
    }
}