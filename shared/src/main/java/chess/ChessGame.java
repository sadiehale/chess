package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }
    private TeamColor teamTurn;
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validPossibilities = new ArrayList<>();
        ChessPiece piece = getBoard().getPiece(startPosition);
        if (piece == null){
            return null;
        }
        TeamColor pieceColor = piece.getTeamColor();

        Collection<ChessMove> movePossibilities = piece.pieceMoves(getBoard(), startPosition);
        for(ChessMove possibility : movePossibilities){
            ChessBoard copiedBoard = getBoard().deepCopy();
            moveOnBoard(copiedBoard, possibility);
            ChessGame attemptedMove = new ChessGame();
            attemptedMove.setBoard(copiedBoard);
            if(!attemptedMove.isInCheck(pieceColor)){
                validPossibilities.add(possibility);
            }
        }

        return validPossibilities;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(piece == null){
            throw new InvalidMoveException("No piece to move / at startPosition");
        }
        if(piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("piece color does not match team color");
        }
        Collection<ChessMove> legalPossibilities = validMoves(move.getStartPosition());
        if (legalPossibilities == null || !legalPossibilities.contains(move)){
            throw new InvalidMoveException("Illegal move for piece");
        }

        moveOnBoard(board, move);
        if (teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
        }else{
            teamTurn = TeamColor.WHITE;
        }
    }


    public void moveOnBoard(ChessBoard board, ChessMove move){
        ChessPiece piece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getStartPosition(), null);
        if(move.getPromotionPiece() != null){
            ChessPiece promoted = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), promoted);
        }else{
            board.addPiece(move.getEndPosition(), piece);
        }
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingSpot = null;
        for(int row = 1; row <= 8; row++){
            for(int col = 1; col<= 8;col++){
                ChessPosition spot = new ChessPosition(row, col);
                ChessPiece piece = getBoard().getPiece(spot);
                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                    kingSpot = spot;
                    break;
                }
            }
        }
        if(kingSpot == null){
            throw new RuntimeException("king not found for current team");
        }

        for(int row = 1; row <= 8; row++){
            for(int col = 1; col<= 8; col++){
                ChessPosition spot = new ChessPosition(row,col);
                ChessPiece piece = getBoard().getPiece(spot);
                if(piece  != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> moves = piece.pieceMoves(getBoard(), spot);
                    for (ChessMove move : moves){
                        if(move.getEndPosition().equals(kingSpot)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            for(int row = 1; row <= 8; row++){
                for(int col = 1; col <= 8; col++){
                    ChessPosition spot = new ChessPosition(row, col);
                    ChessPiece piece = getBoard().getPiece(spot);

                    if(piece != null && piece.getTeamColor() == teamColor){
                        Collection<ChessMove> validPossibilities = validMoves(spot);
                        if(validPossibilities != null && !validPossibilities.isEmpty()){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            for(int row = 1; row <= 8; row++){
                for(int col = 1; col <= 8; col++){
                    ChessPosition spot = new ChessPosition(row, col);
                    ChessPiece piece = getBoard().getPiece(spot);

                    if(piece != null && piece.getTeamColor() == teamColor){
                        Collection<ChessMove> validPossibilities = validMoves(spot);
                        if(validPossibilities != null && !validPossibilities.isEmpty()){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }


    private ChessBoard board;
    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
