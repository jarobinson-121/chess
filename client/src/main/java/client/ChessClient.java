package client;

import exception.ResponseException;
import models.GameData;
import models.GameSummaryList;
import models.UserData;
import server.ServerFacade;
import server.State;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {

    private ServerFacade server;
    private State state = State.SIGNED_OUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public void run() {
        System.out.println(" Welcome to the pet store. Sign in to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> registerUser(params);
                case "login" -> login(params);
                case "list" -> listPets();
                case "signout" -> signOut();
                case "adopt" -> adoptPet(params);
                case "list" -> listGames();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }


}
