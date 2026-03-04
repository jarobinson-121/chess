package service;

import dataaccess.daomodels.AuthDao;
import dataaccess.daomodels.GameDao;

public class CreateGameService {

    private final AuthDao authDao;
    private final GameDao gameDao;

    public CreateGameService(AuthDao authDao, GameDao gameDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
    }


}
