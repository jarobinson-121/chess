package server.websocket;

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

    public void sendToEveryoneElse(Session excludeSession, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        for (Session c : connections) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void sendToEveryone() {

    }

    public void privateMessage(Session userSession, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        userSession.getRemote().sendString(msg);
    }
}