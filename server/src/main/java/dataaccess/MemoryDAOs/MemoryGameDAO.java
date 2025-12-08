package dataaccess.MemoryDAOs;

import chess.ChessGame;
import dataaccess.DAOModels.GameDAO;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, GameData> gameList = new HashMap<>();
    private int nextID = 1;

    // public record GameData(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    //}

    public GameData createGame(String gameName) {
        GameData game = new GameData(nextID++, null, null, gameName, new ChessGame());
        gameList.put(game.gameID(), game);
        return game;
    }

    public GameData getGame(Integer gameID) throws DataAccessException {
        if (gameList.get(gameID) == null) {
            throw new DataAccessException("Game not found");
        }
        return gameList.get(gameID);
    }

    public void updateGame(GameData newGame) throws DataAccessException {
        if (gameList.get(newGame.gameID()) == null) {
            throw new DataAccessException("Game not found");
        }
        gameList.remove(newGame.gameID());
        gameList.put(newGame.gameID(), newGame);
    }

    public Collection<GameData> listGames() {
        return gameList.values();
    }

    public void clearGames() {
        gameList.clear();
    }
}