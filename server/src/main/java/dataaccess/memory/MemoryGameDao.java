package dataaccess.memory;

import chess.ChessGame;
import dataaccess.daomodels.GameDao;
import models.GameData;

import java.util.HashMap;

public class MemoryGameDao implements GameDao {

    private HashMap<String, GameData> gameList = new HashMap<>();
    private int nextID = 1;

    public GameData createGame(String gameName) {
        GameData game = new GameData(nextID++, null, null, gameName, new ChessGame());
        gameList.put(gameName, game);
        return game;
    }

    public GameData getGame(int gameID) {
        return null;
    }

    public void updateGame(ChessGame game) {

    }

    public void clearGames() {

    }
}
