package dataaccess.daomodels;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(Integer gameID) throws DataAccessException;

    void updateGame(GameData newGame) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void clearGames() throws DataAccessException;
}