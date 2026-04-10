package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {

    void loadNotify(LoadGameMessage loadGameMessage);

    void errorNotify(ErrorMessage errorMessage);

    void notify(NotificationMessage notificationMessage);
}
