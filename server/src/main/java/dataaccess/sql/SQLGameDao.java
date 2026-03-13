package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.daomodels.GameDao;
import models.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class SQLGameDao implements GameDao {

    int nextID = 1;

    public GameData createGame(String gameName) throws DataAccessException {
        try {
            int id = nextID++;
            ChessGame game = new ChessGame();
            String gameString = new Gson().toJson(game);
            var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, gameState) " +
                    "VALUES (?, ?, ?, ?, ?)";
            DatabaseManager.executeUpdate(statement, id, null, null, gameName, gameString);
            return new GameData(id, null, null, gameName, game);
        } catch (Exception ex) {
            throw new DataAccessException("Error: Failed to create new game: " + ex.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM games " +
                    "WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: Failed to retrieve game: " + ex.getMessage());
        }
        return null;
    }

    public void updateGame(GameData newGame) {

    }

    public Collection<GameData> listGames() {
        return null;
    }

    public void clearGames() throws DataAccessException {
        var statement = "DELETE FROM games";
        DatabaseManager.executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String gameStateString = rs.getString("gameState");
        ChessGame gameState = new Gson().fromJson(gameStateString, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, gameState);
    }
}
