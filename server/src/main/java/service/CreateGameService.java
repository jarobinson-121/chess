package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

public class CreateGameService {
    private AuthDAO AuthDAO;
    private GameDAO GameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.AuthDAO = authDAO;
        this.GameDAO = gameDAO;
    }

    public GameData createGame(String token, String gameName) throws DataAccessException {
        AuthData user = AuthDAO.getAuth(token);
        if (user == null) {
            throw new DataAccessException("Unauthorized");
        }
        return GameDAO.createGame(gameName);
    }

}