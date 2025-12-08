package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData addUser(UserData user) throws DataAccessException {
        if (users.get(user.username()) != null) {
            throw new DataAccessException("Already taken");
        }
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());

        UserData hashedUser = new UserData(user.username(), hashedPassword, user.email());
        users.put(hashedUser.username(), hashedUser);
        return user;
    }

    public UserData getUserByUsername(String username) throws DataAccessException {
        if (users.get(username) == null) {
            throw new DataAccessException("User not found");
        }
        return users.get(username);
    }

    public void clearUsers() {
        users.clear();
    }
}