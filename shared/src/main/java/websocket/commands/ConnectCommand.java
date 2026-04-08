package websocket.commands;

public class ConnectCommand extends UserGameCommand {

    private final boolean observer;

    public ConnectCommand(String token, Integer gameID, boolean observer) {
        super(CommandType.CONNECT, token, gameID);
        this.observer = observer;
    }
}
