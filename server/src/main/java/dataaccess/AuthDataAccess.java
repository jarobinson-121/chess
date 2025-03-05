package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDataAccess implements AuthDAO {

    private final HashMap<String, AuthData> authList = new HashMap<>();

    public AuthData createAuth(String username) {
        AuthData auth = new AuthData(generateToken(), username);
        authList.put(username, auth);
        return auth;
    }

    public AuthData getAuth(String token) {
        return authList.get(token);
    }

    public void deleteAuth(String username) {
        authList.remove(username);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
