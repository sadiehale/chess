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
            int[][] pawnMoves; //moves in straight line forward one (unless first move)
            int[][] pawnMoves_capturing; //diagonal only when capturing

            if (board.getPiece(myPosition).teamColor == ChessGame.TeamColor.WHITE){ //white team can only move one direction
                pawnMoves_capturing = new int[][] {{+1,+1}, {+1,-1}};
                if (myPosition.getRow() == 2){
                    pawnMoves = new int[][] {{+1,0}, {+2, 0}};
                }else{
                    pawnMoves = new int[][] {{+1,0}};
                }
            }else{
                pawnMoves_capturing = new int[][] {{-1, +1}, {-1,-1}}; //black team moves in opposite direction
                if (myPosition.getRow() == 7){
                    pawnMoves = new int[][] {{-1, 0}, {-2, 0}};
                }else{
                    pawnMoves = new int[][] {{-1, 0}};
                }
            }

            for( int[] possibility : pawnMoves){
                int newRow = row;
                int newCol = col;
                newRow += possibility[0];
                newCol += possibility[1];

                if(inBounds(newRow, newCol)){
                    ChessPosition newSpot = new ChessPosition(newRow, newCol);
                    if (board.getPiece(newSpot) == null) { //check and see if new spot is empty & promote pawn suggestions
                        pawnPromotions(newRow, board, myPosition, newSpot, movePossibilities);
                    }else{
                        break;
                    }
                }
            }

            for(int[] possibility : pawnMoves_capturing){
                int newRow = row;
                int newCol = col;
                newRow += possibility[0];
                newCol += possibility[1];

                if (inBounds(newRow, newCol)){
                    ChessPosition newSpot = new ChessPosition (newRow, newCol);
                    if (board.getPiece(newSpot) != null && (board.getPiece(newSpot).teamColor != board.getPiece(myPosition).teamColor)){
                        pawnPromotions(newRow, board, myPosition, newSpot, movePossibilities);
                    }
                }
            }
        }else if(getPieceType() == PieceType.ROOK){ //moves in straight lines
            //rook
            int[][] rookMoves = {
                    {0, +1}, {0, -1}, {+1, 0}, {-1, 0}
            };
            continuousMovement(rookMoves, myPosition, board, movePossibilities);


        }else if(getPieceType() == PieceType.KNIGHT){ //moves in L shape, 2 in one direction & 1 in other direction
            //knight
            int[][] knightMoves = {
                    {+1,+2}, {+1,-2}, {-1,+2}, {-1, -2}, {+2, +1}, {+2, -1}, {-2,+1}, {-2, -1}
            };
            oneStepMovement(knightMoves, myPosition, board, movePossibilities);

        }else if(getPieceType() == PieceType.BISHOP){ // moves in diagonal lines
            //bishop
            int[][] bishopMoves = {
                    {+1, +1}, {+1, -1}, {-1,-1}, {-1, +1}
            };
            continuousMovement(bishopMoves, myPosition, board, movePossibilities);

        }else if(getPieceType() == PieceType.QUEEN){ //moves in straight and diagonal lines
            //queen
            int[][] queenMoves = {
                    {0, +1}, {0, -1}, {+1, 0}, {-1, 0}, {+1,+1}, {+1,-1}, {-1, -1}, {-1, +1}
            };
            continuousMovement(queenMoves, myPosition, board, movePossibilities);

        }else if(getPieceType() == PieceType.KING) { //moves one square in all directions
            //king
            int[][] kingMoves = {
                    {0,+1}, {0, -1}, {+1,0}, {-1,0}, {+1,+1}, {+1,-1}, {-1,-1}, {-1,+1}
            };
            oneStepMovement(kingMoves, myPosition, board, movePossibilities);

        }
        return movePossibilities;
    }

   private void continuousMovement(int[][] movements, ChessPosition start, ChessBoard board, Collection<ChessMove> moves){
        //for queens, rooks, and bishops
        for (int[] possibility : movements){
            int newRow = start.getRow();
            int newCol = start.getColumn();
            while(true){
                newRow += possibility[0];
                newCol += possibility[1];
                if(!inBounds(newRow, newCol)){
                    break;
                }else{
                    ChessPosition newSpot = new ChessPosition(newRow, newCol);
                    if(canMove(newSpot, board)){
                        moves.add(new ChessMove(start, newSpot, null));
                        if(board.getPiece(newSpot) != null){
                            break;
                        }
                    }else{
                        break;
                    }
                }
            }
        }
   }

   private void oneStepMovement(int[][] movements, ChessPosition start, ChessBoard board, Collection<ChessMove> moves){
        //for kings and knights
        for(int[] possibility : movements){
            int newRow = start.getRow() + possibility[0];
            int newCol = start.getColumn() + possibility[1];
            if(inBounds(newRow, newCol)){
                ChessPosition newSpot = new ChessPosition(newRow, newCol);
                if(canMove(newSpot, board)){
                    moves.add(new ChessMove(start, newSpot, null));
                }
            }
        }
   }

   private void pawnPromotions(int row, ChessBoard board, ChessPosition start, ChessPosition end, Collection<ChessMove> moves){
       //for pawns
        if ((row == 8 && board.getPiece(start).teamColor == ChessGame.TeamColor.WHITE) || (row == 1 && board.getPiece(start).teamColor == ChessGame.TeamColor.BLACK)){
           moves.add(new ChessMove(start, end, PieceType.BISHOP));
           moves.add(new ChessMove(start, end, PieceType.KNIGHT));
           moves.add(new ChessMove(start, end, PieceType.QUEEN));
           moves.add(new ChessMove(start, end, PieceType.ROOK));
       }else{
           moves.add(new ChessMove(start, end, null));
       }
   }

    public boolean inBounds(int row, int col){ //checks if within 8by8 grid
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    public boolean canMove(ChessPosition move, ChessBoard board){ // checks spot
        if (!inBounds(move.getRow(), move.getColumn())){
            return false;
        }
        ChessPiece spotCheck = board.getPiece(move);
        return spotCheck == null || spotCheck.teamColor != this.teamColor;
    }
}
