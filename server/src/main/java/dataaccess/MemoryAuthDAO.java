package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private final HashMap<String, AuthData> authList = new HashMap<>();

    public AuthData createAuth(String username) {
        AuthData auth = new AuthData(generateToken(), username);
        authList.put(auth.authToken(), auth);
        return auth;
    }

    public AuthData getAuth(String token) {
        return authList.get(token);
    }

    public void deleteAuth(String token) {
        authList.remove(token);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}