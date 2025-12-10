package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.LogoutRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public String register(String... params) throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ResponseException {
        if (params.length <= 2 || params.length >= 4) {
            throw new ResponseException(ResponseException.Code.BadRequest, "Expected <USERNAME> <PASSWORD> <EMAIL>");
        }
        UserData userData = new UserData(params[0], params[1], params[2]);
        String userJson = new Gson().toJson(userData);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + "/user"))
                .POST(HttpRequest.BodyPublishers.ofString(userJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return responseChecker(response);
    }

    public String login(String... params) throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ResponseException {
        LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
        String loginJson = new Gson().toJson(loginRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + "/session"))
                .POST(HttpRequest.BodyPublishers.ofString(loginJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return responseChecker(response);
    }

    public String createGame(String token, String... params) throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ResponseException {
        CreateGameRequest createGameRequest = new CreateGameRequest(params[0]);
        String createGameJson = new Gson().toJson(createGameRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + "/game"))
                .header("authorization", token)
                .POST(HttpRequest.BodyPublishers.ofString(createGameJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return responseChecker(response);
    }

    public String listGames(String token) throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ResponseException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + "/game"))
                .header("authorization", token)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return responseChecker(response);
    }

    public String joinGame(String token, String... params) throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ResponseException {
        JoinGameRequest joinGameRequest = new JoinGameRequest(params[1], Integer.parseInt(params[0]));
        String joinJson = new Gson().toJson(joinGameRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + "/game"))
                .header("authorization", token)
                .PUT(HttpRequest.BodyPublishers.ofString(joinJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return responseChecker(response);

    }

    public String logout(String token) throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ResponseException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + "/session"))
                .header("authorization", token)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return responseChecker(response);
    }

    private String responseChecker(HttpResponse<String> response) throws ResponseException {
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
//            System.out.println(response.body());
            return response.body();
        } else {
//            System.out.println("Error: received status code " + response.statusCode());
            throw new ResponseException(ResponseException.fromHttpStatusCode(response.statusCode()), "Server Error");
        }
    }

    public void clearDB() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + "/db"))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            System.out.println(response.body());
            System.out.println("clear successful");
        }
    }

}
