package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;
import exception.ResponseException;
import io.javalin.websocket.*;
import models.AuthData;
import models.GameData;
import models.SessionData;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
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
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> getGame(command.getAuthToken(), command.getGameID(), ctx.session);
                case MAKE_MOVE -> makeMove();
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

        GameData game = gameDao.getGame(gameId);
        AuthData auth = authDao.getAuth(token);

        if (auth == null) {
            connections.privateMessage(session, new ErrorMessage("Error: Unauthorized"));
            return;
        }
        if (game == null) {
            connections.privateMessage(session, new ErrorMessage("Error: Unauthorized"));
            return;
        }

        boolean observer = (!auth.username().equals(game.whiteUsername()) &&
                !auth.username().equals(game.blackUsername()));

        connections.add(session, new SessionData(token, gameId, observer));
        var message = new LoadGameMessage(game);
        connections.privateMessage(session, message);
    }

    private void resign(Session session) throws IOException {
        var msg_string = String.format("%s left the shop");
        var ServerMessage = new ServerMessage(NOTIFICATION);
        connections.sendToEveryoneElse(session, ServerMessage);
        connections.remove(session);
    }

    public void makeMove() {

    }

    public void leaveGame() {

    }

//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var msg_string = String.format("%s says %s", petName, sound);
//            var ServerMessage = new ServerMessage(ServerMessage.Type.NOISE, msg_string);
//            connections.sendToEveryoneElse(null, ServerMessage);
//        } catch (Exception ex) {
//            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
//        }
//    }

}
