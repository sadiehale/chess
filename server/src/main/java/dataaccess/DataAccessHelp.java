package dataaccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import chess.ChessGame;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DataAccessHelp implements dataAccess{
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();
    private final Map<String, AuthData> auths = new HashMap<>();

    private int nextGameID = 1;


    //clear
    @Override
    public void clear(){
        users.clear();
        games.clear();
        auths.clear();
        nextGameID = 1;
    }

    //user
    @Override
    public void insertUser(UserData user) throws DataAccessException{
        if(users.containsKey(user.username())){
            throw new DataAccessException("Username taken");
        }
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username){
        return users.get(username);
    }

    //Game
    @Override
    public int insertGame(GameData game){
        int gameID = nextGameID++;
        GameData fullGame = new GameData(gameID, null, null, game.gameName(), new ChessGame());
        games.put(gameID, fullGame);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID){
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException{
        if(!games.containsKey(game.gameID())){
            throw new DataAccessException("GameID does not exist");
        }
        games.put(game.gameID(),game);
    }

    //Auth
    @Override
    public void insertAuth(AuthData auth){
        auths.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken){
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken){
        auths.remove(authToken);
    }



}
