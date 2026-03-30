package server;

import java.net.http.HttpClient;

import com.google.gson.*;
import exception.ResponseException;
import models.*;
import requests.CreateGameRequest;
import requests.LoginRequest;
import results.CreateGameResult;
import results.ListGamesResult;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData registerUser(String... params) throws ResponseException {
        UserData userJson = (params.length == 3) ? new UserData(params[0], params[1], params[2]) :
                new UserData(params[0], params[1], null);

        HttpRequest request = buildRequest("POST", "/user", null, userJson);

        var response = sendRequest(request);

        return handleResponse(response, AuthData.class);
    }

    public AuthData loginUser(String... params) throws ResponseException {
        LoginRequest loginRequest = new LoginRequest(params[0], params[1]);

        HttpRequest request = buildRequest("POST", "/session", null, loginRequest);

        var response = sendRequest(request);

        return handleResponse(response, AuthData.class);
    }

    public CreateGameResult createGame(String token, String... params) throws ResponseException {
        if (params != null) {
            CreateGameRequest createGameRequest = new CreateGameRequest(params[0]);

            HttpRequest request = buildRequest("POST", "/game", token, createGameRequest);

            var response = sendRequest(request);

            return handleResponse(response, CreateGameResult.class);
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected <NAME>");
    }

    public ListGamesResult listGames(String token) throws ResponseException {
        if (token != null) {
            HttpRequest request = buildRequest("GET", "/game", token, null);

            var response = sendRequest(request);

            return handleResponse(response, ListGamesResult.class);

        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Unable to retrieve");
    }

    private HttpRequest buildRequest(String method, String path, String token, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (token != null) {
            request.setHeader("Authorization", token);
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
