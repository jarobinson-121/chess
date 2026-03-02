package dataaccess.daomodels;

import dataaccess.DataAccessException;
import models.UserData;

public interface UserDao {

    UserData createUser(UserData user) throws DataAccessException;

    UserData getUser(String username);

    void clearUsers();
}
