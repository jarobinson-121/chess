package dataaccess.daomodels;

import dataaccess.DataAccessException;
import models.UserData;

public interface UserDao {

    UserData createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearUsers();
}
