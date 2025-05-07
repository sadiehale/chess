package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "teamColor=" + teamColor +
                ", pieceType=" + pieceType +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    private ChessGame.TeamColor teamColor;
    private PieceType pieceType;

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> movePossibilities = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if(getPieceType() == PieceType.PAWN) {
            //pawn
            return null;
        }else if(getPieceType() == PieceType.ROOK){
            //rook
            return null;
        }else if(getPieceType() == PieceType.KNIGHT){
            //knight
            return null;
        }else if(getPieceType() == PieceType.BISHOP){
            //bishop
            return null;
        }else if(getPieceType() == PieceType.QUEEN){
            //queen
            return null;
        }else if(getPieceType() == PieceType.KING) {
            //king
            int[][] kingMoves = {
                    {0,+1}, {0, -1}, {+1,0}, {-1,0}, {+1,+1}, {+1,-1}, {-1,-1}, {-1,+1}
            };
            for (int[] possibility : kingMoves){
                int newRow = row + possibility[0];
                int newCol = col + possibility[1];

                if (inBounds(newRow, newCol)){
                    ChessPosition newSpot = new ChessPosition(newRow, newCol);
                    if (canMove(newSpot, board)){
                        movePossibilities.add(new ChessMove(myPosition, newSpot, null));
                    }
                }
            }
        }
        return movePossibilities;
    }

    public boolean inBounds(int row, int col){
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    public boolean canMove(ChessPosition move, ChessBoard board){
        if (!inBounds(move.getRow(), move.getColumn())){
            return false;
        }
        ChessPiece spotCheck = board.getPiece(move);
        return spotCheck == null || spotCheck.teamColor != this.teamColor;
    }
}
