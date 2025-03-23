import Service.CreateGameService;
import Service.LoginService;
import Service.LogoutService;
import Service.RegisterService;
import chess.*;
import dataaccess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        var registerService = new RegisterService(authDAO, userDAO);
        var loginService = new LoginService(authDAO, userDAO);
        var logoutService = new LogoutService(authDAO);
        var createGameService = new CreateGameService(authDAO, gameDAO);

        Server server = new Server(userDAO, authDAO, gameDAO, registerService, loginService, logoutService, createGameService);

        server.run(8080);


        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}