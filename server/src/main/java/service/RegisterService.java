package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class RegisterService {
    private AuthDAO AuthDAO;
    private UserDAO UserDAO;

    public RegisterService(AuthDAO authDAO, UserDAO userDAO) {
        this.AuthDAO = authDAO;
        this.UserDAO = userDAO;
    }

    public AuthData createUser(String username, String password, String email) throws DataAccessException {
        if (UserDAO.getUserByUsername(username) != null) {
            throw new DataAccessException("Username already exists");
        }
        UserDAO.addUser(new UserData(username, password, email));
        return AuthDAO.createAuth(username);
    }
}