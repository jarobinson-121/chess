package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import exception.ResponseException;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;
import dataaccess.daomodels.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Types.NULL;

public class SQLUserDao implements UserDao {

    public UserData createUser(UserData user) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        try {
            var statement = "INSERT INTO users (username, passwordHash, email) VALUES (?, ?, ?)";
            DatabaseManager.executeUpdate(statement, user.username(), hashedPassword, user.email());
            return user;
        } catch (Exception ex) {
            throw new DataAccessException("Error: Failed to create new user");
        }
    }

    public UserData getUser(String username) {
        return null;
    }

    public void clearUsers() {

    }
}
