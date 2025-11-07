package service;

import dataaccess.AuthDAO;
import exception.ResponseException;

public class LogoutService {
    private AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logoutUser(String authToken) throws ResponseException {
        var auth = authDAO.getAuth(authToken);
        if (auth == null || auth.username() == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}