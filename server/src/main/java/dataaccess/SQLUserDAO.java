package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {


    @Override
    public UserData addUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, passwordHash, email) VALUES (?, ?, ?)";
        executeUpdate(statement, user.username(), user.password(), user.email());
        return user;
    }

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUsers() {

    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    } else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    } else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to update database", e);
        }
    }

}
