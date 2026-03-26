package client;

import exception.ResponseException;
import models.GameData;
import models.GameSummaryList;
import models.UserData;
import server.ServerFacade;
import server.State;

import java.util.Arrays;
import java.util.Scanner;

import static server.State.SIGNED_IN;
import static server.State.SIGNED_OUT;
import static ui.EscapeSequences.*;

public class ChessClient {

    private ServerFacade server;
    private State state = SIGNED_OUT;

    private String token;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_MAGENTA);
    }

    public void run() {
        System.out.println(BLACK_KING + " Welcome to Chess The Game. Sign in to start." + WHITE_KING);
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (state) {
                case SIGNED_OUT -> evalSignedOut(cmd, params);
                case SIGNED_IN -> evalSignedIn(cmd, params);
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String evalSignedOut(String cmd, String... params) throws ResponseException {
        try {
            return switch (cmd) {
                case "register" -> registerUser(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            throw new ResponseException(ex.code(), ex.getMessage());
        }
    }

    public String evalSignedIn(String cmd, String... params) {
        try {
            return switch (cmd) {
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "list" -> listGames();
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String registerUser(String... params) throws ResponseException {
        if (params.length >= 2 && params.length <= 3) {
            var response = server.registerUser(params);
            token = response.authToken();
            return String.format("Successfully registered as %s", response.username());
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            var response = server.loginUser(params[0], params[1]);
            token = response.authToken();
            return String.format("Successfully logged in as %s", response.username());
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <USERNAME> <PASSWORD>");
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            try {
                return null;
            } catch (Exception ex) {

            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <USERNAME> <PASSWORD>");
    }

    public String listGames() {
        return null;
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            try {
                return null;
            } catch (Exception ex) {

            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <ID> <WHITE | BLACK>");
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            try {
                return null;
            } catch (Exception ex) {

            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <ID>");
    }

    public String logout(String... params) throws ResponseException {
        if (params.length == 0) {
            try {
                return null;
            } catch (Exception ex) {

            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <USERNAME> <PASSWORD>");
    }

    public String help() {
        if (state == SIGNED_OUT) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - login <USERNAME> <PASSWORD>
                    - quit
                    """;
        }
        return """
                - create <NAME>
                - list
                - join <ID> <WHITE | BLACK>
                - observe <ID>
                - logout
                - quit
                - help
                """;
    }

}
