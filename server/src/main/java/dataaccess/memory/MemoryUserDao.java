package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.daomodels.UserDao;
import models.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDao {

    private final HashMap<String, UserData> userList = new HashMap<>();

    public UserData createUser(UserData user) throws DataAccessException {
        if (userList.containsKey(user.username())) {
            throw new DataAccessException("Already taken");
        }
        userList.put(user.username(), user);
        return user;
    }
}
