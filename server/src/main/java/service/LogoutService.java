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
        try {
            AuthData auth = authDao.getAuth(token);
            if (auth == null || auth.username() == null) {
                throw new ResponseException(ResponseException.Code.Unauthorized, "Error: Unauthorized");
            }
            authDao.deleteAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
}
