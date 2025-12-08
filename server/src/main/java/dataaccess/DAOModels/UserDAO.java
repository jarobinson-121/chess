package dataaccess.DAOModels;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDAO {

    UserData addUser(UserData user) throws DataAccessException;

    UserData getUserByUsername(String username) throws DataAccessException;

//    void deleteUser(String username) throws DataAccessException;

    void clearUsers() throws DataAccessException;

}