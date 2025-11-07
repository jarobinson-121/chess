package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

public class RegisterService {
    private AuthDAO AuthDAO;
    private UserDAO UserDAO;

    public RegisterService(AuthDAO authDAO, UserDAO userDAO) {
        this.AuthDAO = authDAO;
        this.UserDAO = userDAO;
    }

    public AuthData createUser(String username, String password, String email) throws ResponseException {
        try {
            UserDAO.addUser(new UserData(username, password, email));
            return AuthDAO.createAuth(username);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.UnameTaken, "Error: already taken");
        }

    }
}