package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage typeChecker = new Gson().fromJson(message, ServerMessage.class);

                        switch (typeChecker.getServerMessageType()) {
                            case LOAD_GAME -> {
                                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                                notificationHandler.loadNotify(loadGameMessage);
                            }
                            case ERROR -> {
                                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                                notificationHandler.errorNotify(errorMessage);
                            }
                            case NOTIFICATION -> {
                                NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                                notificationHandler.notify(notification);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("WS onMessage error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void connect(String token, Integer gameId) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, token, gameId);
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void makeMove(String token, ChessMove move, Integer gameID) throws ResponseException {
        try {
            var command = new MakeMoveCommand(token, gameID, move);
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void resign(String token, Integer gameID) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, token, gameID);
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

}
