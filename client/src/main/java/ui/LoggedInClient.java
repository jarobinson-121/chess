package ui;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import exception.ResponseException;
import model.GameData;
import model.GameListResult;
import model.GameSummary;
import server.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class LoggedInClient {

    private ServerFacade server;

    public LoggedInClient(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input, String token) throws ResponseException {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> createGame(token, params);
                case "list" -> listGames(token);
                case "join" -> joinGame(token, params);
//                case "observe" -> observeGame(params);
                case "logout" -> logout(token);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String createGame(String token, String... params) throws ResponseException {
        try {
            return server.createGame(token, params);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public String listGames(String token) throws ResponseException {
        try {
            var serverResponse = server.listGames(token);
            GameListResult gameList = new Gson().fromJson(serverResponse, GameListResult.class);

            String userFriendlyList = new String();
            int gameNumber = 1;

            for (GameSummary summary : gameList.games()) {

                String whiteUsername = "(open)";
                String blackUsername = "(open)";

                if (summary.whiteUsername() != null) {
                    whiteUsername = summary.whiteUsername();
                }
                if (summary.blackUsername() != null) {
                    blackUsername = summary.blackUsername();
                }

                String formattedSummary = String.format(
                        "%d. %-10s %-15s %-25s %-25s%n",
                        gameNumber++, summary.gameName(),
                        "Game ID: " + summary.gameID(),
                        "White Player: " + whiteUsername,
                        "Black Player: " + blackUsername
                );

                userFriendlyList += formattedSummary;
            }

            return userFriendlyList;
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public String joinGame(String token, String... params) throws ResponseException {
        try {
            return server.joinGame(token, params);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new ResponseException(ResponseException.fromHttpStatusCode(ex.hashCode()), ex.getMessage());
        }
    }

    public String logout(String token) throws ResponseException {
        try {
            server.logout(token);
            return "Logged out";
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }


    public String help() {
        return """
                Options:
                Create a game: "create" <GAMENAME>
                List all games: "list"
                Join a game: "join" <ID> <WHITE|BLACK>
                Observe a game: "observe" <ID>
                Logout: "logout"
                List options: "help"
                Quit: "quit"
                """;
    }
}
