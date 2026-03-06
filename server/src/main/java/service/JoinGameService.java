package service;

import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;
import exception.ResponseException;
import models.AuthData;
import models.GameData;

public class JoinGameService {

    private final AuthDao authDao;
    private final GameDao gameDao;

    public JoinGameService(AuthDao authDao, GameDao gameDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public void joinGame(String token, String playerColor, int gameID) throws ResponseException {
        try {
            AuthData auth = authDao.getAuth(token);
            if (auth == null || auth.username() == null) {
                throw new ResponseException(ResponseException.Code.Unauthorized, "Error: Unauthorized");
            }
            GameData oldGame = gameDao.getGame(gameID);
            if (oldGame == null) {
                throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
            }

            String whiteUsername = oldGame.whiteUsername();
            String blackUsername = oldGame.blackUsername();
            if (blackUsername != null && whiteUsername != null) {
                throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Error: Unavailable");
            }
            if (playerColor == null) {
                throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
            } else if (playerColor.toLowerCase().equals("white")) {
                if (oldGame.whiteUsername() != null) {
                    throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Error: Player taken");
                }
                whiteUsername = auth.username();
            } else if (playerColor.toLowerCase().equals("black")) {
                if (oldGame.blackUsername() != null) {
                    throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Error: Player taken");
                }
                blackUsername = auth.username();
            } else {
                throw new ResponseException(ResponseException.Code.BadRequest, "Error: Bad Request");
            }
            gameDao.updateGame(new GameData(gameID, whiteUsername, blackUsername, oldGame.gameName(), oldGame.game()));
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, "Error: Server error");
        }
    }
}
