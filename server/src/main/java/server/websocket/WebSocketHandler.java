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
import websocket.messages.ServerMessage;

import java.io.IOException;

import static websocket.messages.ServerMessage.ServerMessageType.*;

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
                connections.privateMessage(session, new ErrorMessage("Error: Cool your jets, you can't move " +
                        "on their turn"));
                return;
            }

            game.game().makeMove(command.getMove());
            gameDao.updateGame(game);

            LoadGameMessage loadGameMessage = new LoadGameMessage(game);

            connections.sendToEveryone(null, loadGameMessage, game.gameID());

        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }


    }

    private void resign(Session session) throws IOException {
        var msg_string = String.format("%s left the shop");
        var ServerMessage = new ServerMessage(NOTIFICATION);
        connections.sendToEveryone(session, ServerMessage, null);
        connections.remove(session);
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
}
