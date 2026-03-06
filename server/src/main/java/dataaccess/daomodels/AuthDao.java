package dataaccess.daomodels;

import dataaccess.DataAccessException;
import models.AuthData;


public interface AuthDao {

    AuthData createAuth(String username);

    AuthData getAuth(String token) throws DataAccessException;

    void deleteAuth(String token) throws DataAccessException;

    void clearAuths() throws DataAccessException;
}
