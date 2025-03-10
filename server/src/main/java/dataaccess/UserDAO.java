package dataaccess;

import model.UserData;

public interface UserDAO {

    UserData addUser(UserData user) throws DataAccessException;

    UserData getUserByEmail(String email) throws DataAccessException;

    UserData getUserByUsername(String username) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;

}
