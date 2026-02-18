package dataaccess.daomodels;

import chess.ChessGame;
import models.GameData;

public interface GameDao {

    GameData createGame(String gameName);

    GameData getGame(int GameID);

    void updateGame(ChessGame newGame);

    void clearGames();
}
