package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard gameBoard;
    private ChessBoard testBoard;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.gameBoard = new ChessBoard();
        gameBoard.resetBoard();
        testBoard = null;
    }

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
        teamTurn = team;
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
        ChessBoard board = boardChecker();
        ChessPiece piece = board.getPiece(startPosition);
        if(piece != null) {
            Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
            Iterator<ChessMove> iterator = moves.iterator();

            while(iterator.hasNext()) {
                ChessMove currMove = iterator.next();
                System.out.println(currMove.toString());

                if(!testMoves(currMove)) {
                    iterator.remove();
                }
            }
            // need to filter to check if any of the moves would put the king in check
            return moves;
        }
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = gameBoard.getPiece(start);
        Collection<ChessMove> valMoves = validMoves(start);

        if(gameBoard.getPiece(start) != null && valMoves.contains(move) && piece.getTeamColor() == teamTurn) {
            if(promo == null) {
                gameBoard.setPiece(end, piece);
            }
            else {
                gameBoard.setPiece(end, new ChessPiece(teamTurn, promo));
            }

            gameBoard.clearPiece(start);
            if(teamTurn == TeamColor.BLACK) {
                teamTurn = TeamColor.WHITE;
            }
            else {
                teamTurn = TeamColor.BLACK;
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard board = boardChecker();

        ChessPosition myKing = findKingPos(teamColor);
        if(myKing == null) {
            return true;
        }

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i + 1, j + 1));
                if(piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> currPieceMoves = piece.pieceMoves(board, new ChessPosition(i + 1, j + 1));
                    Iterator<ChessMove> iterator = currPieceMoves.iterator();
                    while(iterator.hasNext()) {
                        ChessMove currMove = iterator.next();
                        if(currMove.getEndPosition().equals(myKing)) {
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
        ChessBoard board = boardChecker();

        if(isInCheck(teamColor)) {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i + 1, j + 1));
                    if(piece != null && piece.getTeamColor() == teamColor) {
                        if(!validMoves(new ChessPosition(i + 1, j + 1)).isEmpty()) {
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
        ChessBoard board = boardChecker();

        if(!isInCheck(teamColor)) {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i + 1, j + 1));
                    if(piece != null && piece.getTeamColor() == teamColor) {
                        if(!validMoves(new ChessPosition(i + 1, j + 1)).isEmpty()) {
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
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    public boolean testMoves(ChessMove move) {
        testBoard = makeTempBoard();

        ChessPiece piece = testBoard.getPiece(move.getStartPosition());

        try {
            makeTestMove(move);
            if(isInCheck(piece.getTeamColor())) {
                testBoard = null;
                return false;
            } else {
                testBoard = null;
                return true;
            }
        }
        catch (InvalidMoveException e) {
            testBoard = null;
            return false;
        }
    }

    public ChessBoard makeTempBoard() {
        ChessBoard copy = new ChessBoard();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                ChessPiece piece = gameBoard.getPiece(new ChessPosition(i + 1, j + 1));
                if(piece != null) {
                    copy.addPiece(new ChessPosition(i + 1, j + 1), piece);
                }
            }
        }
        return copy;
    }

    public void makeTestMove(ChessMove move) throws InvalidMoveException {

        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = testBoard.getPiece(start);

        if(promo == null) {
            testBoard.setPiece(end, piece);
        }
        else {
            testBoard.setPiece(end, new ChessPiece(teamTurn, promo));
        }

        testBoard.clearPiece(start);
    }

    public ChessPosition findKingPos(TeamColor teamColor) {
        ChessBoard board = boardChecker();

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i + 1, j + 1));
                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(i + 1, j + 1);
                }
            }
        }
        return null;
    }

    public ChessBoard boardChecker() {
        if(testBoard != null) {
            return testBoard;
        }
        else {
            return gameBoard;
        }
    }
}
