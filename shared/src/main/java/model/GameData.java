package model;

public class GameData {
    public record game(int gameID, String whiteUsername, String blackUsername, String gameName, game ChessGame){}
}
