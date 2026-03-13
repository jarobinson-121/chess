package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.daomodels.AuthDao;
import models.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDao implements AuthDao {

    public AuthData createAuth(String username) throws DataAccessException {
        try {
            String token = generateToken();
            var statement = "INSERT INTO auths (token, username) VALUES (?, ?)";
            DatabaseManager.executeUpdate(statement, token, username);
            return new AuthData(token, username);
        } catch (Exception ex) {
            throw new DataAccessException("Error: Failed to add authorization");
        }
    }

    public AuthData getAuth(String token) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT token, username FROM auths where token=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, token);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: Failed to create new auth" + ex.getMessage());
        }
        return null;
    }

    public void deleteAuth(String token) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE token=?";
        DatabaseManager.executeUpdate(statement, token);
    }

    public void clearAuths() throws DataAccessException {
        var statement = "TRUNCATE auths";
        DatabaseManager.executeUpdate(statement);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var token = rs.getString("token");
        var username = rs.getString("username");

        return new AuthData(token, username);
    }
}
