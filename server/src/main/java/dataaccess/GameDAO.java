package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public interface GameDAO {

    GameData createGame(String gameName);

    GameData getGame(Integer gameID) throws DataAccessException;

    void updateGame(GameData newGame) throws DataAccessException;

    Collection<GameData> listGames();

    void deleteGame(String gameID) throws DataAccessException;

    void clearGames();
}