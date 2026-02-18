package dataaccess.daomodels;

import models.UserData;

public interface UserDao {

    UserData createUser(String username, String password);

    UserData getUser(String username);

    void clearUsers();
}
