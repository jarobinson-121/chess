package service;

import dataaccess.daomodels.AuthDAO;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;

public class LogoutService {
    private AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logoutUser(String authToken) throws DataAccessException, ResponseException {
        AuthData auth;
        try {
            auth = authDAO.getAuth(authToken);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
        if (auth == null || auth.username() == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}