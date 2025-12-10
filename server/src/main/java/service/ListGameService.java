package service;

import dataaccess.daomodels.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.daomodels.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.GameListResult;
import model.GameSummary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class ListGameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public ListGameService(AuthDAO auth, GameDAO game) {
        this.authDAO = auth;
        this.gameDAO = game;
    }

    public GameListResult listGames(String token) throws DataAccessException, ResponseException {
        AuthData user;
        try {
            user = authDAO.getAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
        if (user == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }

        Collection<GameData> fullList = gameDAO.listGames();

        List<GameSummary> summaryList = new ArrayList<>();

        for (GameData game : fullList) {
            var newSummary = new GameSummary(game.gameID(),
                    game.whiteUsername(),
                    game.blackUsername(),
                    game.gameName());

            summaryList.add(newSummary);
        }

        return new GameListResult(summaryList);
    }
}
