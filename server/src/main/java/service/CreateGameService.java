package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;

public class CreateGameService {
    private AuthDAO AuthDAO;
    private GameDAO GameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.AuthDAO = authDAO;
        this.GameDAO = gameDAO;
    }

    public GameData createGame(String token, String gameName) throws ResponseException {
        AuthData user;
        try {
            user = AuthDAO.getAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.Unauthorized, ex.getMessage());
        }
        if (user == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }
        if (gameName == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Empty game name");
        }
        return GameDAO.createGame(gameName);
    }

}