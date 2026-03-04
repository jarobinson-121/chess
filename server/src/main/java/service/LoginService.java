package service;

import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.UserDao;
import exception.ResponseException;
import models.AuthData;
import models.UserData;

public class LoginService {

    private final AuthDao authDao;
    private final UserDao userDao;

    public LoginService(AuthDao authDao, UserDao userDao) {
        this.authDao = authDao;
        this.userDao = userDao;
    }

    public AuthData loginUser(String username, String password) throws ResponseException {
        UserData user;
        try {
            user = userDao.getUser(username);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
        if (user == null || user.username() == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        if (user.password() != password) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        return authDao.createAuth(username);
    }
}
