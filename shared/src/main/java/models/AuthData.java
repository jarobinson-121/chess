package models;

public record AuthData(String authToken, String username) {

    public String getAuth() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
