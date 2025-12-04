package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public interface GameDAO {

    GameData createGame(String gameName);

    GameData getGame(Integer gameID);

    void updateGame(GameData newGame);

    Collection<GameData> listGames();

    void deleteGame(String gameID);
}