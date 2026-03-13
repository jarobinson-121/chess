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
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDao implements GameDao {


    public GameData createGame(String gameName) throws DataAccessException {
        try {
            ChessGame game = new ChessGame();
            String gameString = new Gson().toJson(game);
            var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, gameState) " +
                    "VALUES (?, ?, ?, ?)";
            int id = DatabaseManager.executeUpdate(statement, null, null, gameName, gameString);
            return new GameData(id, null, null, gameName, game);
        } catch (Exception ex) {
            throw new DataAccessException("Error: Failed to create new game: " + ex.getMessage());
        }
    }

    public GameData getGame(Integer gameID) throws DataAccessException {
        if (gameID == null) {
            return null;
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM games " +
                    "WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                    return null;
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: Failed to retrieve game: " + ex.getMessage());
        }

    }

    public void updateGame(GameData newGame) throws DataAccessException {
        GameData oldGame = getGame(newGame.gameID());
        if (oldGame == null) {
            throw new DataAccessException("Error: Game not found");
        }

        String gameString = new Gson().toJson(newGame.game());
        var statement = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, gameState=? " +
                "WHERE gameID=?";
        DatabaseManager.executeUpdate(statement,
                newGame.whiteUsername(),
                newGame.blackUsername(),
                newGame.gameName(),
                gameString,
                newGame.gameID());
    }

    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> gameList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        gameList.add(readGame(rs));
                    }
                    return gameList;
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: unable to red game(s) from database: " + ex.getMessage());
        }
    }

    public void clearGames() throws DataAccessException {
        var statement = "TRUNCATE games";
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
