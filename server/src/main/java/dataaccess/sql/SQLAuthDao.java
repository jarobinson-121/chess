package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.daomodels.AuthDao;
import models.AuthData;
import org.mindrot.jbcrypt.BCrypt;

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

    public AuthData getAuth(String token) {
        return null;
    }

    public void deleteAuth(String token) {

    }

    public void clearAuths() {

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
