package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQLUserDAOTest {

    static SQLUserDAO userDAO;

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        DatabaseInitializer.configureDatabase();
        userDAO = new SQLUserDAO();
    }

    @BeforeEach
    void emptyUserDB() throws DataAccessException {
        userDAO.clearUsers();
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
    void getUserByUsernamePositive() throws DataAccessException {
        UserData user = new UserData("username", "password", "email@website");
        userDAO.addUser(user);

        UserData getUser = userDAO.getUserByUsername("username");
        Assertions.assertNotNull(getUser);
        Assertions.assertEquals("username", getUser.username());
        Assertions.assertEquals("password", getUser.password());
        Assertions.assertEquals("email@website", getUser.email());
    }

    @Test
    void getNonexistentUserNegative() throws DataAccessException {
        UserData user = new UserData("username", "password", "email@website");
        userDAO.addUser(user);

        Assertions.assertNull(userDAO.getUserByUsername("notAUser"));
    }

    @Test
    void clearUsers() throws DataAccessException {
        userDAO.addUser(new UserData("username0", "password", "email@website"));
        userDAO.addUser(new UserData("username1", "password", "email@website"));
        userDAO.addUser(new UserData("username2", "password", "email@website"));

        userDAO.clearUsers();

        Assertions.assertNull(userDAO.getUserByUsername("username0"));
        Assertions.assertNull(userDAO.getUserByUsername("username1"));
        Assertions.assertNull(userDAO.getUserByUsername("username2"));
    }
}