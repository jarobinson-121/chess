package dataaccess.requests;

import com.google.gson.Gson;

public record JoinGameRequest(String playerColor, Integer gameID) {
    public String toString() {
        return new Gson().toJson(this);
    }
}
