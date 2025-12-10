package ui;

import exception.ResponseException;
import server.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

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
//                case "create" -> createGame(params);
//                case "list" -> list(params);
//                case "join" -> joinGame(params);
//                case "observe" -> observeGame(params);
                case "logout" -> logout(token);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
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
