package dataaccess.daomodels;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDAO {

    AuthData createAuth(String token, String username) throws DataAccessException;

    AuthData getAuth(String token) throws DataAccessException;

    void deleteAuth(String token) throws DataAccessException;

    void clearAuths() throws DataAccessException;

}