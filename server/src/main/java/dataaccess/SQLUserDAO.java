package dataaccess;

import dataaccess.daomodels.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {


    @Override
    public UserData addUser(UserData user) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());

        var statement = "INSERT INTO users (username, passwordHash, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, user.username(), hashedPassword, user.email());
        return user;
    }

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, passwordHash, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("unable to read user from database", e);
        }
        return null;
    }

    @Override
    public void clearUsers() throws DataAccessException {
        var statement = "DELETE FROM users";
        DatabaseManager.executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var passwordHash = rs.getString("passwordHash");
        var email = rs.getString("email");

        return new UserData(username, passwordHash, email);
    }
}
