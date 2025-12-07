package dataaccess;

import model.AuthData;

public interface AuthDAO {

    AuthData createAuth(String token, String username) throws DataAccessException;

    AuthData getAuth(String token) throws DataAccessException;

    void deleteAuth(String token);

    void clearAuths() throws DataAccessException;

}