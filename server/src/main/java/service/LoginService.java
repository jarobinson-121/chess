package service;

import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.UserDao;
import exception.ResponseException;
import models.AuthData;

public class LoginService {

    private final AuthDao authDao;
    private final UserDao userDao;

    public LoginService(AuthDao authDao, UserDao userDao) {
        this.authDao = authDao;
        this.userDao = userDao;
    }

    public AuthData loginUser(String username, String Password) throws ResponseException {
        try {
            userDao.getUser(username);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
        return authDao.createAuth(username);
    }
}
