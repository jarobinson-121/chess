package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class ListGameService {
    private AuthDAO AuthDAO;
    private GameDAO GameDAO;

    public ListGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.AuthDAO = authDAO;
        this.GameDAO = gameDAO;
    }

    public HashMap<String, Object> listGames(String token) throws ResponseException {
        AuthData user;
        try {
            user = AuthDAO.getAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.Unauthorized, ex.getMessage());
        }
        if (user == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Unauthorized");
        }

        Collection<GameData> fullList = GameDAO.listGames();

        ArrayList<HashMap<String, Object>> editedList = new ArrayList<>();

        for (GameData game : fullList) {
            HashMap<String, Object> currGame = new HashMap<>();
            currGame.put("gameID", game.gameID());
            currGame.put("whiteUsername", game.whiteUsername());
            currGame.put("blackUsername", game.blackUsername());
            currGame.put("gameName", game.gameName());

            editedList.add(currGame);
        }

        HashMap<String, Object> output = new HashMap<>();
        output.put("games", editedList);

        return output;
    }
}
