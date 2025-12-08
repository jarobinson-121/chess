package service;

import dataaccess.daomodels.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.daomodels.UserDAO;
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
            if (ex.getMessage().contains("unable to update database")) {
                throw new ResponseException(ResponseException.Code.AlreadyTakenError, ex.getMessage());
            }
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }

    }
}