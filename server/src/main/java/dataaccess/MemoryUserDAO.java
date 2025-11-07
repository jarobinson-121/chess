package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData addUser(UserData user) throws DataAccessException {
        if (users.get(user.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
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