package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

class SQLUserDAOTest {

    static SQLUserDAO userDAO;
    UserData user = new UserData("username", "password", "email@website");

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        DatabaseManager.configureDatabase();
        userDAO = new SQLUserDAO();
    }

    @BeforeEach
    void emptyUserDB() throws DataAccessException {
        userDAO.clearUsers();
    }

    private void getAddAssertions(UserData userData) throws DataAccessException {
        userDAO.addUser(userData);

        String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

        UserData getUser = userDAO.getUserByUsername("username");
        Assertions.assertNotNull(getUser);
        Assertions.assertEquals("username", getUser.username());
        Assertions.assertTrue(BCrypt.checkpw("password", hashedPassword));
        Assertions.assertEquals("email@website", getUser.email());
    }


    @Test
    void addUserPositive() throws DataAccessException {
        getAddAssertions(user);
    }

    @Test
    void addUserUsernameTaken() throws DataAccessException {
        userDAO.addUser(user);

        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(user);
        });
    }

    @Test
    void getUserByUsernamePositive() throws DataAccessException {
        getAddAssertions(user);
    }

    @Test
    void getNonexistentUserNegative() throws DataAccessException {
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