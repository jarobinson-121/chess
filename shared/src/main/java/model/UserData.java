package model;

import com.google.gson.*;

public record UserData(String username, String password, String email) {

    public String getUsername() {
        return username;
    }

    // might need to remove this later because security duh
    public String getPassword() {
        return password;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
