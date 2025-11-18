package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {

    GameData createGame(String gameName);

    GameData getGame(String gameID);

    GameData joinGame(String playerColor, String gameID);

    HashMap<Integer, GameData> listGames();

    void deleteGame(String gameID);
}