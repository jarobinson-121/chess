package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.websocket.*;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

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
                case CONNECT -> enter(command.getCommandType(), ctx.session);
                case MAKE_MOVE -> makeMove();
                case RESIGN -> exit(command.getCommandType(), ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void enter(UserGameCommand.CommandType type, String token, Session session) throws IOException {
        connections.add(session);
//        var msg_string = String.format("%s is in the shop",);
        var message = new ServerMessage(NOTIFICATION);
        connections.broadcast(session, message);
    }

    private void exit(String visitorName, Session session) throws IOException {
        var msg_string = String.format("%s left the shop", visitorName);
        var ServerMessage = new ServerMessage(NOTIFICATION);
        connections.broadcast(session, ServerMessage);
        connections.remove(session);
    }

    public void makeMove() {

    }

    public void makeNoise(String petName, String sound) throws ResponseException {
        try {
            var msg_string = String.format("%s says %s", petName, sound);
            var ServerMessage = new ServerMessage(ServerMessage.Type.NOISE, msg_string);
            connections.broadcast(null, ServerMessage);
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

}
