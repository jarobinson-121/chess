package requests;

import com.google.gson.*;

public record CreateGameRequest(String gameName) {

    public String toString() {
        return new Gson().toJson(this);
    }

}