package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;
import exception.ResponseException;
import io.javalin.websocket.*;
import models.AuthData;
import models.GameData;
import models.SessionData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDao gameDao;
    private final AuthDao authDao;

    public WebSocketHandler(AuthDao authDao, GameDao gameDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            System.out.println("Server received WS message: " + ctx.message());
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> getGame(command.getAuthToken(), command.getGameID(), ctx.session);
                case MAKE_MOVE -> makeMove(ctx);
                case LEAVE -> leaveGame();
                case RESIGN -> resign(ctx.session);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void getGame(String token, Integer gameId, Session session) throws IOException,
            DataAccessException {

        System.out.println("Handling CONNECT command");
        GameData game = gameDao.getGame(gameId);
        AuthData auth = authDao.getAuth(token);
        String color = null;

        if (auth == null) {
            connections.privateMessage(session, new ErrorMessage("Error: Unauthorized"));
            return;
        }
        if (game == null) {
            connections.privateMessage(session, new ErrorMessage("Error: Invalid Game"));
            return;
        }

        boolean whitePlayer = (auth.username().equals(game.whiteUsername())) ? true : false;
        boolean blackPlayer = (auth.username().equals(game.blackUsername())) ? true : false;

        boolean observer = (!whitePlayer && !blackPlayer);

        if (!observer) {
            color = (whitePlayer) ? "WHITE" : "BLACK";
        }

        connections.add(session, new SessionData(token, gameId, observer));
        LoadGameMessage loadGameMessage = new LoadGameMessage(game);
        connections.privateMessage(session, loadGameMessage);

        NotificationMessage notification = new NotificationMessage(notificationTextBuilder(auth, observer, color));
        connections.sendToEveryone(session, notification, gameId);
    }

    public void makeMove(WsMessageContext ctx) throws ResponseException {
        MakeMoveCommand command = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
        try {
            Session session = ctx.session;

            GameData game = gameDao.getGame(command.getGameID());
            AuthData auth = authDao.getAuth(command.getAuthToken());

            if (!goodAuthGame(session, auth, game)) {
                return;
            }

            ChessGame.TeamColor color = (game.whiteUsername().equals(auth.username())) ? ChessGame.TeamColor.WHITE :
                    ChessGame.TeamColor.BLACK;

            if (color != game.game().getTeamTurn()) {
                connections.privateMessage(session, new ErrorMessage("Error: You may only move on your turn"));
                return;
            }

            var validMoves = game.game().validMoves(command.getMove().getStartPosition());
            if (validMoves.isEmpty()) {
                connections.privateMessage(session, new ErrorMessage("No valid moves for this piece."));
                return;
            } else if (!validMoves.contains(command.getMove())) {
                connections.privateMessage(session, new ErrorMessage("Error: Invalid move"));
                return;
            }

            if (inCheckmate(game) || inStalemate(game)) {
                connections.privateMessage(session, new ErrorMessage("Game complete. No moves allowed"));
                return;
            }

            game.game().makeMove(command.getMove());
            gameDao.updateGame(game);

            LoadGameMessage loadGameMessage = new LoadGameMessage(game);

            if (inCheckmate(game) || inStalemate(game)) {
                connections.privateMessage(session, new ErrorMessage("Game complete. No moves allowed"));
                return;
            }

            inCheck(game);

            connections.sendToEveryone(null, loadGameMessage, game.gameID());

        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }


    }

    private void resign(Session session) throws IOException {

    }

    public void leaveGame() {

    }

    private String notificationTextBuilder(AuthData auth, boolean observer, String color) {
        if (observer == true) {
            return auth.username() + " is now observing the game.";
        } else {
            return auth.username() + " has joined the game as " + color;
        }
    }

    private boolean goodAuthGame(Session session, AuthData authData, GameData gameData) throws IOException {
        if (authData == null) {
            connections.privateMessage(session, new ErrorMessage("Error: Unauthorized"));
            return false;
        }
        if (gameData == null) {
            connections.privateMessage(session, new ErrorMessage("Error: Unauthorized"));
            return false;
        }
        return true;
    }

    private boolean inCheck(GameData game) throws IOException {
        if (game.game().isInCheck(ChessGame.TeamColor.WHITE)) {
            connections.sendToEveryone(null, new NotificationMessage("White is in check"),
                    game.gameID());
            return true;
        } else if (game.game().isInCheck(ChessGame.TeamColor.BLACK)) {
            connections.sendToEveryone(null, new NotificationMessage("Black is in check"),
                    game.gameID());
            return true;
        }
        return false;
    }

    private boolean inStalemate(GameData game) throws IOException {
        if (game.game().isInStalemate(ChessGame.TeamColor.WHITE)) {
            connections.sendToEveryone(null, new NotificationMessage("Stalemate reached"),
                    game.gameID());
            return true;
        } else if (game.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
            connections.sendToEveryone(null, new NotificationMessage("Stalemate reached"),
                    game.gameID());
            return true;
        }
        return false;
    }

    private boolean inCheckmate(GameData game) throws IOException {
        if (game.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            connections.sendToEveryone(null, new NotificationMessage("White is in checkmate"),
                    game.gameID());
            return true;
        } else if (game.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            connections.sendToEveryone(null, new NotificationMessage("Black is in checkmate"),
                    game.gameID());
            return true;
        }
        return false;
    }
}
