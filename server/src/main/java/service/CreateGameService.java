package service;

import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;
import exception.ResponseException;
import models.AuthData;
import models.GameData;
import models.UserData;
import requests.CreateGameRequest;

public class CreateGameService {

    private final AuthDao authDao;
    private final GameDao gameDao;

    public CreateGameService(AuthDao authDao, GameDao gameDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public GameData createGame(String token, String gameName) throws ResponseException {
        AuthData auth;
        try {
            auth = authDao.getAuth(token);
            if (auth == null) {
                throw new ResponseException(ResponseException.Code.Unauthorized, "Error: Unauthorized");
            }
            if (gameName == null) {
                throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
            }
            return gameDao.createGame(gameName);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
}
