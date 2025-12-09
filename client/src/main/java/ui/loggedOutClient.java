package ui;

import exception.ResponseException;
import model.UserData;
import server.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;

public class loggedOutClient {

    public enum State {
        SIGNEDOUT,
        SIGNEDIN
    }

    private final ServerFacade server;
    private State state;

    public loggedOutClient(int url) {
        server = new ServerFacade(url);
        state = State.SIGNEDOUT;
    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type Help to get started.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Exception ex) {
                var msg = ex.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }


    public String eval(String input) throws ResponseException {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            try {
                server.register(params);
                state = State.SIGNEDIN;
                return String.format("You signed in as %s", params[0]);
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
            }
        }
        return null;
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            try {
                server.login(params);
                state = State.SIGNEDIN;
                return String.format("You logged in as %s", params[0]);
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
            }
        }
        return null;
    }

    public String help() {
        return """
                Options:
                Login as an existing user: "login" <USERNAME> <PASSWORD>
                Register a new user: "register" <USERNAME> <PASSWORD> <EMAIL>
                List options: "help"
                Quit: "quit"
                """;
    }

}
