package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    GameData createGame(String gameName);

    GameData getGame(Integer gameID) throws DataAccessException;

    void updateGame(GameData newGame) throws DataAccessException;

    Collection<GameData> listGames();

    void clearGames();
}