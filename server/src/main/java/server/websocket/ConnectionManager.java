package server.websocket;

import com.google.gson.*;
import exception.ResponseException;
import models.SessionData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, SessionData> connections = new ConcurrentHashMap<>();

    public void add(Session session, SessionData data) {
        connections.put(session, data);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void sendToEveryone(Session excludeSession,
                               ServerMessage notification,
                               Integer gameId) {
        String msg = new Gson().toJson(notification);
        for (var each : connections.entrySet()) {
            Session session = each.getKey();
            SessionData data = each.getValue();
            Integer id = data.gameId();

            if (id.equals(gameId)) {
                if (excludeSession == null || !session.equals(excludeSession)) {
                    try {
                        session.getRemote().sendString(msg);
                    } catch (IOException ex) {
                        connections.remove(session);
                    }

                }
            }
        }
    }

    public void privateMessage(Session userSession, ServerMessage notification) throws IOException {
        String msg = new Gson().toJson(notification);
        userSession.getRemote().sendString(msg);
    }
}