package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;
import dataaccess.daomodels.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDao implements UserDao {

    public UserData createUser(UserData user) throws DataAccessException {
        try {
            String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
            var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            DatabaseManager.executeUpdate(statement, user.username(), hashedPassword, user.email());
            return user;
        } catch (Exception ex) {
            throw new DataAccessException("Error: Failed to create new user" + ex.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users where username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: Failed to create new user" + ex.getMessage());
        }
        return null;
    }

    public void clearUsers() throws DataAccessException {
        var statement = "TRUNCATE users";
        DatabaseManager.executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");

        return new UserData(username, password, email);
    }
}
