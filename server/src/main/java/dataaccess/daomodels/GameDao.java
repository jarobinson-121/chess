package dataaccess.daomodels;

import chess.ChessGame;
import dataaccess.DataAccessException;
import models.GameData;

import java.util.Collection;

public interface GameDao {

    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData newGame) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void clearGames() throws DataAccessException;
}
