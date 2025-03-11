package dataaccess;

import com.google.gson.*;

public record LogoutRequest(String authToken) {

    public String toString() {
        return new Gson().toJson(this);
    }
}
