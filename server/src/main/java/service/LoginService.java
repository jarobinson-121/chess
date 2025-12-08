package service;

import exception.ResponseException;
import model.AuthData;
import dataaccess.DataAccessException;
import dataaccess.DAOModels.AuthDAO;
import dataaccess.DAOModels.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
        if (user == null || user.username() == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        String hashedUserPW = user.password();
        if (!BCrypt.checkpw(password, hashedUserPW)) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        return authDAO.createAuth(UUID.randomUUID().toString(), username);
    }
}