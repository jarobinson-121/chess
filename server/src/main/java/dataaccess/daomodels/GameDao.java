package dataaccess.daomodels;

import chess.ChessGame;
import dataaccess.DataAccessException;
import models.GameData;

public interface GameDao {

    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(int GameID) throws DataAccessException;

    void updateGame(GameData newGame) throws DataAccessException;

    void clearGames();
}
