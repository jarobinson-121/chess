package service;

import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import exception.ResponseException;
import models.AuthData;

public class LogoutService {

    private final AuthDao authDao;

    public LogoutService(AuthDao authDao) {
        this.authDao = authDao;
    }

    public void logoutUser(String token) throws ResponseException {
        AuthData auth;
        try {
            auth = authDao.getAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
        if (auth == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Error: Unauthorized");
        }
        try {
            authDao.deleteAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
}
