package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    private AuthDAO AuthDAO;
    private GameDAO GameDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.AuthDAO = authDAO;
        this.GameDAO = gameDAO;
    }

    public void joinGame(String token, String color, Integer gameID) throws DataAccessException {
        AuthData user = AuthDAO.getAuth(token);
        String uname = user.username();
        if (user == null) {
            throw new DataAccessException("Unauthorized");
        }
        GameData oldGame = GameDAO.getGame(gameID);
        if (oldGame.blackUsername() != null && oldGame.whiteUsername() != null) {
            throw new DataAccessException("Already Taken");
        }
        String blackUname = oldGame.blackUsername();
        String whiteUname = oldGame.whiteUsername();
        if ("WHITE".equals(color) && oldGame.whiteUsername() == null) {
            whiteUname = uname;
        } else if ("BLACK".equals(color) && oldGame.blackUsername() == null) {
            blackUname = uname;
        }
        GameDAO.updateGame(new GameData(oldGame.gameID(), whiteUname, blackUname, oldGame.gameName(), oldGame.game()));
    }
}
