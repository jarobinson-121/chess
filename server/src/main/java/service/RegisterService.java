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

    public RegisterService(AuthDAO auth, UserDAO user) {
        this.AuthDAO = auth;
        this.UserDAO = user;
    }

    public AuthData createUser(String username, String password, String email) throws ResponseException {
        try {
            UserDAO.addUser(new UserData(username, password, email));
            return AuthDAO.createAuth(username);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.AlreadyTakenError, "already taken");
        }

    }
}