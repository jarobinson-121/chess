package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();

//    AuthData auth = new AuthData(generateToken(), username);
//        authList.put(username, auth);
//        return auth;

    public UserData addUser(UserData user) throws DataAccessException {
        users.put(user.username(), user);
        return user;
    }

    public UserData getUserByEmail(String email) throws DataAccessException {
        return users.get(email);

    }

    public UserData getUserByUsername(String username) throws DataAccessException {
        return users.get(username);
    }

    public void deleteUser(String username) throws DataAccessException {
        users.remove(username);
    }
}
