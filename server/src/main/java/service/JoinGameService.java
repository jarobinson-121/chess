package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    private AuthDAO AuthDAO;
    private GameDAO GameDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.AuthDAO = authDAO;
        this.GameDAO = gameDAO;
    }

    public void joinGame(String token, String color, Integer gameID) throws DataAccessException, ResponseException {
        AuthData user;
        try {
            user = AuthDAO.getAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.Unauthorized, ex.getMessage());
        }
        String uname = user.username();
        if (user == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }

        GameData oldGame;
        try {
            oldGame = GameDAO.getGame(gameID);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.BadRequest, ex.getMessage());
        }
        if (oldGame.blackUsername() != null && oldGame.whiteUsername() != null) {
            throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Unauthorized");
        }

        String blackUname = oldGame.blackUsername();
        String whiteUname = oldGame.whiteUsername();
        if (color == null) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Color field missing");
        } else if ("WHITE".equals(color)) {
            if (whiteUname != null) {
                throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Color already taken");
            }
            whiteUname = uname;
        } else if ("BLACK".equals(color)) {
            if (blackUname != null) {
                throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Color already taken");
            }
            blackUname = uname;
        } else {
            throw new ResponseException(ResponseException.Code.BadRequest, "Invalid color");
        }

        GameDAO.updateGame(new GameData(oldGame.gameID(), whiteUname, blackUname, oldGame.gameName(), oldGame.game()));
    }
}
