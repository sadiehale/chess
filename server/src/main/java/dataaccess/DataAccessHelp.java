package dataaccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import chess.ChessGame;

import java.util.HashMap;
import java.util.Map;

public class DataAccessHelp implements DataAccess{
    private final Map<String, UserData> users = new HashMap<>();
}
