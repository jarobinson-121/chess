package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private final HashMap<String, AuthData> authList = new HashMap<>();

    public AuthData createAuth(String token, String username) {
        AuthData auth = new AuthData(generateToken(), username);
        authList.put(auth.authToken(), auth);
        return auth;
    }

    public AuthData getAuth(String token) throws DataAccessException {
        if (authList.get(token) == null) {
            throw new DataAccessException("Error: User not found");
        }
        return authList.get(token);
    }

    public void deleteAuth(String token) {
        authList.remove(token);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void clearAuths() {
        authList.clear();
    }
}