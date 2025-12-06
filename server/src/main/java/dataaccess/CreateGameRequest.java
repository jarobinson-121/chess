package dataaccess;

import com.google.gson.*;

public record CreateGameRequest(String playerColor, String gameName) {

    public String toString() {
        return new Gson().toJson(this);
    }

}