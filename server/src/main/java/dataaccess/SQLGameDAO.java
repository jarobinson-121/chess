package dataaccess;

import com.google.gson.Gson;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO {

    int nextID = 1;

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        int id = nextID++;
        var game = new ChessGame();
        var gameString = new Gson().toJson(game);
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, gameState) VALUES " +
                "(?, ?, ?, ?, ?)";
        executeUpdate(statement, id, null, null, gameName, gameString);
        return new GameData(id, null, null, gameName, game);
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData newGame) throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void clearGames() {

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
