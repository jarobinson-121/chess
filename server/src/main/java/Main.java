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
//        TODO: Uncomment this line when you've added the memoryGameDAO and have soemthing to use it on.
//        GameDAO gameDAO = new MemoryGameDAO();

        var registerService = new RegisterService(authDAO, userDAO);
        var loginService = new LoginService(authDAO, userDAO);
        var logoutService = new LogoutService(authDAO);

        Server server = new Server(userDAO, authDAO, registerService, loginService, logoutService);

        server.run(8080);


        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}