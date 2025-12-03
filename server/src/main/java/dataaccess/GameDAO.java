package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {

    GameData createGame(String gameName);

    GameData getGame(Integer gameID);

    void updateGame(GameData newGame);

    HashMap<Integer, GameData> listGames();

    void deleteGame(String gameID);
}