package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData createAuth(String token, String username) throws DataAccessException {
        var statement = "INSERT INTO auths (token, username) VALUES (?, ?)";
        executeUpdate(statement, token, username);
        return new AuthData(token, username);
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT token, username FROM auths WHERE token=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, token);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("unable to read auth from database", e);
        }
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE token=?";
        executeUpdate(statement, token);
    }

    @Override
    public void clearAuths() throws DataAccessException {
        var statement = "DELETE FROM auths";
        executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var token = rs.getString("token");
        return new AuthData(token, username);
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
