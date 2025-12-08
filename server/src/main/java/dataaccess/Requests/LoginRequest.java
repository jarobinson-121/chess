package dataaccess.Requests;

import com.google.gson.*;

public record LoginRequest(String username, String password) {

    public String toString() {
        return new Gson().toJson(this);
    }
}