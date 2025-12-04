package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
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

    public HashMap<String, Object> listGames(String token) throws DataAccessException {
        AuthData user = AuthDAO.getAuth(token);
        if (user == null) {
            throw new DataAccessException("Unauthorized");
        }

        Collection<GameData> fullList = GameDAO.listGames();

        ArrayList<HashMap<String, Object>> editedList = new ArrayList<>();

        for (GameData game : fullList) {
            HashMap<String, Object> currGame = new HashMap<>();
            currGame.put("gameID:", game.gameID());
            currGame.put("whiteUsername:", game.whiteUsername());
            currGame.put("blackUsername", game.blackUsername());
            currGame.put("gameName", game.gameName());

            editedList.add(currGame);
        }

        HashMap<String, Object> output = new HashMap<>();
        output.put("games", editedList);

        return output;
    }
}
