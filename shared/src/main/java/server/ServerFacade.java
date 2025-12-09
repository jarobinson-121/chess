package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final String serverUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String... params) throws URISyntaxException, IOException, InterruptedException {
        UserData userData = new UserData(params[0], params[1], params[2]);
        String userJson = new Gson().toJson(userData);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + "/user"))
                .POST(HttpRequest.BodyPublishers.ofString(userJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return responseChecker(response);
    }

    public AuthData login(String... params) throws URISyntaxException, IOException, InterruptedException {
        AuthData authData = new AuthData(params[0], params[1]);
        String authJson = new Gson().toJson(authData);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + "/session"))
                .POST(HttpRequest.BodyPublishers.ofString(authJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return responseChecker(response);
    }

    private AuthData responseChecker(HttpResponse<String> response) {
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            System.out.println(response.body());
            AuthData responseAuth = new Gson().fromJson(response.body(), AuthData.class);
            return responseAuth;
        } else {
            System.out.println("Error: received status code " + response.statusCode());
        }
        return new AuthData("FailedToke", "failed username");
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
