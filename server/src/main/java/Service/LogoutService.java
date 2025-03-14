package Service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;

public class LogoutService {
    private AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logoutUser(String authToken) throws DataAccessException {
        var auth = authDAO.getAuth(authToken);
        if(auth.username() == null) {
            throw new DataAccessException("No matching user");
        }
        authDAO.deleteAuth(authToken);
    }
}
