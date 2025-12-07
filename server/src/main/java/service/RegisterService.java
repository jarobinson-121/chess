package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    private AuthDAO authDAO;
    private UserDAO userDAO;

    public RegisterService(AuthDAO auth, UserDAO user) {
        this.authDAO = auth;
        this.userDAO = user;
    }

    public AuthData createUser(String username, String password, String email) throws ResponseException {
        try {
            userDAO.addUser(new UserData(username, password, email));
            return authDAO.createAuth(UUID.randomUUID().toString(), username);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.AlreadyTakenError, "already taken");
        }

    }
}