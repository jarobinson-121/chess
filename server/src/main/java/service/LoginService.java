package service;

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

    public AuthData loginUser(String username, String password) throws DataAccessException {
        var user = userDAO.getUserByUsername(username);
        if (user.username() == null) {
            throw new DataAccessException("No user found");
        }
        if (!user.password().equals(password)) {
            throw new DataAccessException("Invalid credentials");
        }
        return authDAO.createAuth(username);
    }
}