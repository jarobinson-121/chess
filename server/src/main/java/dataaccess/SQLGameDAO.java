package dataaccess;

import com.google.gson.Gson;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO {

    int nextID = 1;

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        if (gameName == null || gameName.isBlank()) {
            throw new DataAccessException("Empty game name");
        }
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
        if (gameID == null) {
            return null;
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("unable to read game from database", e);
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var stateString = rs.getString("gameState");
        ChessGame gameState = new Gson().fromJson(stateString, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, gameState);
    }

    @Override
    public void updateGame(GameData newGame) throws DataAccessException {
        GameData oldGame = getGame(newGame.gameID());
        if (oldGame == null) {
            throw new DataAccessException("Game not found");
        }

        String gameString = new Gson().toJson(newGame.game());
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameState = ?" +
                "  WHERE gameID=?";
        executeUpdate(statement,
                newGame.whiteUsername(),
                newGame.blackUsername(),
                newGame.gameName(),
                gameString,
                newGame.gameID());
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> games = new ArrayList<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("unable to read game(s) from database", e);
        }
        return games;
    }

    @Override
    public void clearGames() throws DataAccessException {
        nextID = 1;
        var statement = "DELETE FROM games";
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
