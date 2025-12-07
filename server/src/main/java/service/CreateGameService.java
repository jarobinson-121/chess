package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;

public class CreateGameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public CreateGameService(AuthDAO auth, GameDAO game) {
        this.authDAO = auth;
        this.gameDAO = game;
    }

    public GameData createGame(String token, String gameName) throws DataAccessException, ResponseException {
        AuthData user;
        try {
            user = authDAO.getAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.Unauthorized, ex.getMessage());
        }
        if (user == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        if (gameName == null) {
            throw new DataAccessException("Empty game name");
        }
        return gameDAO.createGame(gameName);
    }

}