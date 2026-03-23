package client;

import server.ServerFacade;

public class ChessClient {

    private ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

}
