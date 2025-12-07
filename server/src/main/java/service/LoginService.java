package service;

import exception.ResponseException;
import model.AuthData;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.UserData;

import java.util.UUID;

public class LoginService {
    private AuthDAO authDAO;
    private UserDAO userDAO;

    public LoginService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthData loginUser(String username, String password) throws DataAccessException, ResponseException {
        UserData user;
        try {
            user = userDAO.getUserByUsername(username);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.Unauthorized, ex.getMessage());
        }
        if (user == null || user.username() == null || !user.password().equals(password)) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        return authDAO.createAuth(UUID.randomUUID().toString(), username);
    }
}