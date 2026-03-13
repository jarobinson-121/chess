package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.daomodels.UserDao;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

public class MemoryUserDao implements UserDao {

    private final HashMap<String, UserData> userList = new HashMap<>();

    public UserData createUser(UserData user) throws DataAccessException {
        try {
            String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
            UserData hashed = new UserData(user.username(), hashedPassword, user.email());
            userList.put(user.username(), hashed);
            return hashed;
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
