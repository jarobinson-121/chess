package dataaccess;

import dataaccess.sql.SQLGameDao;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeAll;

public class SQLGameDaoTests {

    static SQLGameDao gameDao;

    @BeforeAll
    static void setUp() throws DataAccessException, ResponseException {
        DatabaseManager.configureDatabase();
    }
    
}
