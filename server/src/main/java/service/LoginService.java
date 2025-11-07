package service;

import exception.ResponseException;
import model.AuthData;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;

public class LoginService {
    private AuthDAO authDAO;
    private UserDAO userDAO;

    public LoginService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthData loginUser(String username, String password) throws DataAccessException, ResponseException {
        var user = userDAO.getUserByUsername(username);
        if (user == null || user.username() == null || !user.password().equals(password)) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        return authDAO.createAuth(username);
    }
}