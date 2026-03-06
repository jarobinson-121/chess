package service;

import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;
import dataaccess.daomodels.UserDao;
import exception.ResponseException;

public class ClearDBService {

    private final AuthDao authDao;
    private final GameDao gameDao;
    private final UserDao userDao;

    public ClearDBService(AuthDao authDao, GameDao gameDao, UserDao userDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
        this.userDao = userDao;
    }

    public void clearDB() throws ResponseException {
        try {
            authDao.clearAuths();
            userDao.clearUsers();
            gameDao.clearGames();
        } catch (DataAccessException e) {
            throw new ResponseException(ResponseException.Code.ServerError, "Error: Server Error");
        }
    }
}
