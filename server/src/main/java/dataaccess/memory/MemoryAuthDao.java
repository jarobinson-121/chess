package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.daomodels.AuthDao;
import models.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDao implements AuthDao {

    private HashMap<String, AuthData> authList = new HashMap<>();

    public AuthData createAuth(String username) {
        AuthData newAuth = new AuthData(generateToken(), username);
        authList.put(newAuth.authToken(), newAuth);
        return newAuth;
    }

    public AuthData getAuth(String token) throws DataAccessException {
        AuthData auth = authList.get(token);
        if (auth == null) {
            throw new DataAccessException("Error: user not found");
        } else {
            return auth;
        }
    }

    public void deleteAuth(String token) throws DataAccessException {
        if (authList.get(token) == null) {
            throw new DataAccessException("Error: user not found");
        } else {
            authList.remove(token);
        }
    }

    public void clearAuths() {
        authList.clear();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "MemoryAuthDao{" +
                "authList=" + authList +
                '}';
    }
}
