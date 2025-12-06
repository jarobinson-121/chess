package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public JoinGameService(AuthDAO auth, GameDAO game) {
        this.authDAO = auth;
        this.gameDAO = game;
    }

    public void joinGame(String token, String color, Integer gameID) throws DataAccessException, ResponseException {
        AuthData user;
        try {
            user = authDAO.getAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.Unauthorized, ex.getMessage());
        }
        String uname = user.username();
        if (user == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }

        GameData oldGame;
        try {
            oldGame = gameDAO.getGame(gameID);
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

        gameDAO.updateGame(new GameData(oldGame.gameID(), whiteUname, blackUname, oldGame.gameName(), oldGame.game()));
    }
}
