package service;

import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.UserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;

public class RegisterService {

    private final AuthDao authDao;
    private final UserDao userDao;

    public RegisterService(AuthDao authDao, UserDao userDao) {
        this.authDao = authDao;
        this.userDao = userDao;
    }

    public AuthData createUser(UserData user) throws ResponseException {
        try {
            userDao.createUser(user);
            return authDao.createAuth(user.username());
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
}
