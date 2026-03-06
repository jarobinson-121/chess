package service;

import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;
import exception.ResponseException;
import models.AuthData;
import models.GameData;
import models.GameSummary;
import models.GameSummaryList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListGamesService {

    private final AuthDao authDao;
    private final GameDao gameDao;

    public ListGamesService(AuthDao authDao, GameDao gameDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public GameSummaryList listGames(String token) throws ResponseException {
        try {
            AuthData auth = authDao.getAuth(token);
            if (auth == null) {
                throw new ResponseException(ResponseException.Code.Unauthorized, "Error: Unauthorized");
            }
            Collection<GameData> fullGameList = gameDao.listGames();

            List<GameSummary> cleanedGameList = new ArrayList<>();

            for (GameData game : fullGameList) {
                GameSummary summarized = new GameSummary(
                        game.gameID(),
                        game.whiteUsername(),
                        game.blackUsername(),
                        game.gameName()
                );

                cleanedGameList.add(summarized);
            }

            return new GameSummaryList(cleanedGameList);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, "Error: Server error");
        }
    }
}
