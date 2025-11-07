package dataaccess;

import com.google.gson.*;

public record CreateGameRequest(String PlayerColor, String gameName) {

    public String toString() {
        return new Gson().toJson(this);
    }

}