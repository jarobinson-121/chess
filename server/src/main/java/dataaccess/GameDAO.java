package dataaccess;

import model.GameData;

public interface GameDAO {

    GameData createGame(String gameName);

    GameData getGame(String gameID);

    GameData joinGame(String playerColor, String gameID);

    void deleteGame(String gameID);
}
