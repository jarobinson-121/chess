package service;

import dataaccess.DAOModels.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DAOModels.GameDAO;
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
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
        if (user == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        if (gameName == null || gameName.isBlank()) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Bad Request");
        }
        return gameDAO.createGame(gameName);
    }

}