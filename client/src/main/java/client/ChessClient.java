package client;

import chess.ChessGame;
import exception.ResponseException;
import models.GameData;
import models.GameSummary;
import server.ServerFacade;
import server.State;
import ui.DrawBoard;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static server.State.*;
import static ui.EscapeSequences.*;

public class ChessClient implements NotificationHandler {

    private ServerFacade server;
    private WebSocketFacade ws;
    private State state = SIGNED_OUT;

    private String token;
    private final Map<Integer, GameSummary> lastListedGames = new HashMap<>();
    private int gameID;
    private String playerColor;
    private GameData currentGame;

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
        ws = new WebSocketFacade(serverUrl, this);
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_MAGENTA);
    }

    public void run() {
        System.out.println(BLACK_KING + " Welcome to Chess The Game. Sign in to start." + WHITE_KING);
        System.out.print(help());
        System.out.print(SET_TEXT_COLOR_BLUE);

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

    public void errorNotify(ErrorMessage errorMessage) {
        System.out.println(SET_TEXT_COLOR_RED + errorMessage.getErrorMessage());
        printPrompt();
    }

    public void loadNotify(LoadGameMessage loadGameMessage) {
        currentGame = loadGameMessage.getGame();
        System.out.println(SET_TEXT_COLOR_BLUE + loadGameMessage.getGame());
        printPrompt();
    }

    public void notify(NotificationMessage notificationMessage) {
        System.out.println(SET_TEXT_COLOR_BLUE + notificationMessage.getMessage());
        printPrompt();
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (state) {
                case SIGNED_OUT -> evalSignedOut(cmd, params);
                case SIGNED_IN -> evalSignedIn(cmd, params);
                case PLAYING_GAME -> evalPlayer(cmd, params);
                case OBSERVER -> evalObserver(cmd, params);
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

    public String evalPlayer(String cmd, String... params) {
        try {
            switch (cmd) {
                case "exit":
                    state = SIGNED_IN;
                    return "Exited game, returning to menu.\n" + help();
                default:
                    help();
            }
            ;
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return null;
    }

    public String evalObserver(String cmd, String... params) {
        try {
            switch (cmd) {
                case "exit":
                    state = SIGNED_IN;
                    return "Exited observation, returning to menu.\n" + help();
                default:
                    help();
            }
            ;
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return null;
    }

    public String registerUser(String... params) throws ResponseException {
        if (params.length >= 2 && params.length <= 3) {
            var response = server.registerUser(params);
            token = response.authToken();
            state = SIGNED_IN;
            return String.format("Successfully registered as %s", response.username());
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            var response = server.loginUser(params[0], params[1]);
            token = response.authToken();
            state = SIGNED_IN;
            return String.format("Successfully logged in as %s", response.username());
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <USERNAME> <PASSWORD>");
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            var response = server.createGame(token, params);
            lastListedGames.put(lastListedGames.size() + 1,
                    new GameSummary(response.gameID(), null, null, null));
            return String.format("Successfully created game: %d", response.gameID());
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <NAME>");
    }

    public String listGames() throws ResponseException {
        var response = server.listGames(token);

        if (response.games().isEmpty()) {
            return "No games right now.";
        }

        lastListedGames.clear();

        StringBuilder output = new StringBuilder();
        int clientId = 1;

        for (GameSummary game : response.games()) {
            lastListedGames.put(clientId, game);

            output.append(String.format("%d. %s | White Player: %s | Black Player : %s \n",
                    clientId++,
                    game.gameName(),
                    game.whiteUsername(),
                    game.blackUsername()));
        }

        return output.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            int localId = validNum(params[0]);
            if (lastListedGames.containsKey(localId)) {
                server.joinGame(token, lastListedGames.get(localId).gameID(), params[1]);
                gameID = Integer.parseInt(params[0]);
                playerColor = params[1];
                state = PLAYING_GAME;

                DrawBoard drawBoard = new DrawBoard(playerColor, currentGame);
                drawBoard.main(playerColor);

                return "Successfully joined game: " + gameID;
            }
            throw new ResponseException(ResponseException.Code.BadRequest, "Invalid game ID. " +
                    "Use list command to view active games.");
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <ID> <WHITE | BLACK>");
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            int localId = validNum(params[0]);
            if (lastListedGames.containsKey(localId)) {
                state = OBSERVER;
                DrawBoard drawBoard = new DrawBoard("white", currentGame);
                drawBoard.main("white");

                return "Observing game: " + localId;
            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <ID>");
    }

    public String logout(String... params) throws ResponseException {
        if (params.length == 0) {
            server.logout(token);
            state = SIGNED_OUT;
            token = null;

            return "Successfully logged out.";
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected logout");
    }

    public String help() {
        if (state == SIGNED_OUT) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - login <USERNAME> <PASSWORD>
                    - quit
                    - help
                    """;
        } else if (state == SIGNED_IN) {
            return """
                    - create <NAME>
                    - list
                    - join <ID> <WHITE | BLACK>
                    - observe <ID>
                    - logout
                    - quit
                    - help
                    """;
        } else if (state == PLAYING_GAME) {
            return """
                    - exit
                    - help
                    """;
        }
        return """
                - exit
                - help
                """;
    }

    private int validNum(String param) throws ResponseException {
        try {
            int number = Integer.parseInt(param);
            return number;
        } catch (NumberFormatException e) {
            throw new ResponseException(ResponseException.Code.BadRequest, "ID must be a number.");
        }
    }

}
