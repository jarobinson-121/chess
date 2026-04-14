package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
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

import java.util.*;

import static server.State.*;
import static ui.EscapeSequences.*;

public class ChessClient implements NotificationHandler {

    private ServerFacade server;
    private WebSocketFacade ws;
    private State state = SIGNED_OUT;

    private String token;
    private final Map<Integer, GameSummary> lastListedGames = new HashMap<>();
    private int gameID;
    private ChessGame.TeamColor playerColor;
    private GameData currentGame;
    Scanner scanner = new Scanner(System.in);

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
        boardDrawHelper(playerColor.toString().toLowerCase());
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
                case "move":
                    return makeMove(params);
                case "redraw":
                    return redraw();
                case "highlight":
                    return highlight(params);
                case "resign":
                    System.out.print("Are you sure you want to resign? <YES> | <NO>");
                    String confirmation = scanner.nextLine();
                    if (resignChecker(confirmation)) {
                        return resign();
                    }
                    return help();
                case "leave":
                    leaveGame();
                    state = SIGNED_IN;
                    return "Leaving game, returning to menu.\n" + help();
                default:
                    return help();
            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String evalObserver(String cmd, String... params) {
        try {
            switch (cmd) {
                case "redraw":
                    return redraw();
                case "highlight":
                    return highlight(params);
                case "exit":
                    state = SIGNED_IN;
                    return "Exited observation, returning to menu.\n" + help();
                case "leave":
                    leaveGame();
                    state = SIGNED_IN;
                    return "Leaving game, returning to menu.\n" + help();
                default:
                    return help();
            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
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
            return String.format("Successfully created game: %d", lastListedGames.size());
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
                gameID = lastListedGames.get(localId).gameID();
                playerColor = stringToColor(params[1]);
                state = PLAYING_GAME;
                System.out.println("Sending CONNECT for game " + gameID);
                ws.connect(token, lastListedGames.get(localId).gameID());

                return "Successfully joined game: " + gameID;
            }
            throw new ResponseException(ResponseException.Code.BadRequest, "Invalid game ID. " +
                    "Use list command to view active games.");
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <ID> <WHITE | BLACK>");
    }

    public String makeMove(String... params) throws ResponseException {
        if (params.length >= 2 && params.length <= 3) {
            ChessPosition start = makePosition(params[0].toLowerCase());
            ChessPosition end = makePosition(params[1].toLowerCase());
            ChessPiece.PieceType promotionPiece = null;

            if (params.length == 3) {
                promotionPiece = stringToPieceType(params[2]);
            }

            ws.makeMove(token, new ChessMove(start, end, promotionPiece), gameID, params[0], params[1]);
            return "";
        }
        throw new ResponseException(ResponseException.Code.BadRequest,
                "Expected <START POSITION> <END POSITION> (optional) <PROMOTION PIECE>");
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            int localId = validNum(params[0]);
            if (lastListedGames.containsKey(localId)) {
                gameID = lastListedGames.get(localId).gameID();
                state = OBSERVER;
                playerColor = ChessGame.TeamColor.WHITE;
                ws.connect(token, gameID);

                return "Observing game: " + localId;
            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <ID>");
    }

    public String highlight(String... params) throws ResponseException {
        if (params.length == 1) {
            ChessPosition selectedPiece = makePosition(params[0]);
            Collection<ChessMove> wholeMoves = currentGame.game().validMoves(selectedPiece);
            Collection<ChessPosition> endPositions = new ArrayList<>();
            for (ChessMove move : wholeMoves) {
                endPositions.add(move.getEndPosition());
            }
            DrawBoard drawBoard = new DrawBoard(playerColor.toString().toLowerCase(), currentGame);
            drawBoard.main(selectedPiece, endPositions);
            return "Valid moves highlighted";
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <POSITION> \n " +
                "Positions must be in the form a1 - h8");
    }

    public String redraw() throws ResponseException {
        if (currentGame != null) {
            boardDrawHelper(playerColor.toString().toLowerCase());
            return "";
        }
        throw new ResponseException(ResponseException.Code.ServerError, "Error: No game to draw");
    }

    public String leaveGame() throws ResponseException {
        ws.leaveGame(token, gameID);
        return "Left game";
    }

    public String resign() throws ResponseException {
        ws.resign(token, gameID);
        return "";
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
                    - move <START POSITION> <END POSITION> (optional)<PROMOTION PIECE>
                    - redraw
                    - highlight <POSITION>
                    - resign
                    - leave
                    - help
                    """;
        }
        return """
                - redraw
                - highlight <POSITION>
                - leave
                - help
                """;
    }

    private void boardDrawHelper(String color) {
        DrawBoard drawBoard = new DrawBoard(color, currentGame);
        drawBoard.main(null, null);
        printPrompt();
    }

    private boolean resignChecker(String input) throws ResponseException {
        input = input.toLowerCase();
        if (input.equals("yes")) {
            return true;
        } else if (input.equals("no")) {
            return false;
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <YES> | <NO>");
    }

    private int validNum(String param) throws ResponseException {
        try {
            int number = Integer.parseInt(param);
            return number;
        } catch (NumberFormatException e) {
            throw new ResponseException(ResponseException.Code.BadRequest, "ID must be a number.");
        }
    }

    private ChessPosition makePosition(String combo) throws ResponseException {
        if (combo.length() != 2) {
            throw new ResponseException(ResponseException.Code.BadRequest,
                    "Position must be in form a1-h8");
        }

        int col = charToInt(combo.charAt(0));
        int row = combo.charAt(1) - '0';

        if (row < 1 || row > 8) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Row must be 1-8");
        }

        return new ChessPosition(row, col);
    }

    private int charToInt(char letter) throws ResponseException {
        return switch (letter) {
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> throw new ResponseException(ResponseException.Code.BadRequest, "Column must be a-h");
        };
    }

    private ChessPiece.PieceType stringToPieceType(String string) throws ResponseException {
        return switch (string.toLowerCase()) {
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new ResponseException(ResponseException.Code.BadRequest,
                    "Promotion piece must be queen, rook, bishop, or knight");
        };
    }

    private ChessGame.TeamColor stringToColor(String color) throws ResponseException {
        return switch (color.toLowerCase()) {
            case "white" -> ChessGame.TeamColor.WHITE;
            case "black" -> ChessGame.TeamColor.BLACK;
            default -> throw new ResponseException(ResponseException.Code.BadRequest,
                    "Color must be either white or black");
        };
    }

}
