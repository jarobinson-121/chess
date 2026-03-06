package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.daomodels.UserDao;
import models.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDao {

    private final HashMap<String, UserData> userList = new HashMap<>();

    public UserData createUser(UserData user) throws DataAccessException {
        try {
            userList.put(user.username(), user);
            return user;
        } catch (Exception ex) {
            throw new DataAccessException("Error: Server Error");
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try {
            return userList.get(username);
        } catch (Exception ex) {
            throw new DataAccessException("Error: Server Error");
        }
    }

    public void clearUsers() {
        userList.clear();
    }
}
