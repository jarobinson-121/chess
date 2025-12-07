package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        return null;
    }

    @Override
    public void deleteAuth(String token) {

    }

    @Override
    public void clearAuths() throws DataAccessException {
        var statement = "DELETE FROM auths";
        executeUpdate(statement);
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
