package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class MainClient {

    LoggedOutClient loggedOutClient;
    LoggedInClient loggedInClient;
    private String token;

    public enum State {
        SIGNEDOUT,
        SIGNEDIN
    }

    private final ServerFacade server;
    public State state;

    public MainClient(int url) {
        server = new ServerFacade(url);
        state = State.SIGNEDOUT;
        loggedOutClient = new LoggedOutClient(server);
        loggedInClient = new LoggedInClient(server);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type 'help' to get started.");
        System.out.print(loggedOutClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                if (state == State.SIGNEDOUT) {
                    if (line.contains("register") || line.contains("login")) {
                        result = loggedOutClient.eval(line);
                        AuthData responseAuth = new Gson().fromJson(result, AuthData.class);
                        token = responseAuth.authToken();
                        System.out.print("You logged in as: " + responseAuth.username());
                        state = State.SIGNEDIN;
                    } else if (line.equals("help")) {
                        System.out.print(loggedOutClient.help());
                    } else if (line.equals("quit")) {
                        result = line;
                    }

                } else if (state == State.SIGNEDIN) {
                    System.out.print(loggedInClient.help());
                    if (line.equals("help")) {
                        System.out.print(loggedInClient.help());
                    } else if (line.equals("quit")) {
                        result = line;
                    } else {
                        result = loggedInClient.eval(line, token);
                        System.out.print(result);
                        if (result.equals("Logged out")) {
                            state = State.SIGNEDOUT;
                        }
                    }
                }
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

}
