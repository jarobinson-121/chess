package dataaccess;

import model.AuthData;

public interface AuthDAO {

    AuthData createAuth(AuthData authData);

    AuthData getAuth(String token);

    void deleteAuth(AuthData authData);

}
