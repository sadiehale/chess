import chess.*;
import spark.Spark;
import java.util.UUID;
public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Spark.port(8080);
        Spark.get("/", (req, res) -> "Server working on port 8080");
    }

    public static String generateToken(){
        return UUID.randomUUID().toString(); //generates random authToken
    }
}