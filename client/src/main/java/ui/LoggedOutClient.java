package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import server.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class LoggedOutClient {

    private final ServerFacade server;

    public LoggedOutClient(ServerFacade server) {
        this.server = server;
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            try {
                return server.register(params);
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 3) {
            try {
                server.login(params);
                return String.format("You logged in as %s", params[0]);
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
            }
        }
        return null;
    }

    public String eval(String input) throws ResponseException {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                Options:
                Login as an existing user: "login" <USERNAME> <PASSWORD>
                Register a new user: "register" <USERNAME> <PASSWORD> <EMAIL>
                List options: "help"
                Quit: "quit"
                """;
    }

}
