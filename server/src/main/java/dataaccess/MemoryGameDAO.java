package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> gameList = new HashMap<>();
    private int nextID = 0;

    // public record GameData(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    //}

    public GameData createGame(String gameName) {
        GameData game = new GameData(nextID++, null, null, gameName, new ChessGame());
        gameList.put(game.GameID(), game);
        return game;
    }

    public GameData getGame(String gameID) {
        return gameList.get(gameID);
    }

    public GameData joinGame(String playerColor, String GameID) {
        GameData game = gameList.get(GameID);
        gameList.remove(GameID);
        return game;
    }

    public GameData updateGame(String gameID) {
        GameData game = gameList.get(gameID);
        return game;
    }

    public void deleteGame(String gameID) {
        gameList.remove(gameID);
    }
}
