package dataaccess.memory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.daomodels.GameDao;
import models.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDao implements GameDao {

    private HashMap<Integer, GameData> gameList = new HashMap<>();
    private int nextID = 1;

    public GameData createGame(String gameName) throws DataAccessException {
        try {
            GameData game = new GameData(nextID++, null, null, gameName, new ChessGame());
            gameList.put(game.gameID(), game);
            return game;
        } catch (Exception ex) {
            throw new DataAccessException("Error: Server Error");
        }

    }

    public GameData getGame(int gameID) throws DataAccessException {
        try {
            gameList.get(gameID);
            return gameList.get(gameID);
        } catch (Exception ex) {
            throw new DataAccessException("Error: Server Error");
        }
    }

    public void updateGame(GameData game) throws DataAccessException {
        try {
            gameList.remove(game.gameID());
            gameList.put(game.gameID(), game);
        } catch (Exception ex) {
            throw new DataAccessException("Error: Server Error");
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        try {
            return gameList.values();
        } catch (Exception ex) {
            throw new DataAccessException("Error: Server Error");
        }
    }

    public void clearGames() throws DataAccessException {
        try {
            gameList.clear();
        } catch (Exception ex) {
            throw new DataAccessException("Error: Server Error");
        }
    }
}
