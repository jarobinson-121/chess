package chess;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

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
        this.testBoard = new ChessBoard();
        gameBoard.resetBoard();
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
        ChessPiece piece = gameBoard.getPiece(startPosition);

        if (piece != null) {
            Collection<ChessMove> moves = piece.pieceMoves(gameBoard, startPosition);
            Iterator<ChessMove> iterator = moves.iterator();

            while (iterator.hasNext()) {
                ChessMove currMove = iterator.next();
//                System.out.println(currMove.toString());

                if (!testMove(currMove)) {
                    iterator.remove();
                }
            }
            return moves;
        }
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = gameBoard.getPiece(start);
        ChessPiece capture = gameBoard.getPiece(end);

        if (piece != null && validMoves(start).contains(move) && piece.getTeamColor() == teamTurn) {
            if (promo == null) {
                gameBoard.addPiece(end, piece);
            } else {
                gameBoard.addPiece(end, new ChessPiece(teamTurn, promo));
            }

            gameBoard.addPiece(start, null);

            setTeamTurn((teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
        } else {
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

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = gameBoard.getPiece(new ChessPosition(i + 1, j + 1));
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> currPieceMoves = piece.pieceMoves(gameBoard, new ChessPosition(i + 1, j + 1));
                    Iterator<ChessMove> iterator = currPieceMoves.iterator();
                    while (iterator.hasNext()) {
                        ChessMove currMove = iterator.next();
                        if (currMove.getEndPosition().equals(gameBoard.getKingPos(teamColor))) {
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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

    public boolean testMove(ChessMove move) {

        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece mover = gameBoard.getPiece(start);
        ChessPiece capture = gameBoard.getPiece(end);
        boolean validMove = true;
        TeamColor moverColor = teamTurn;
        TeamColor currTurn = teamTurn;

        doTestMove(move);

        if (isInCheck(moverColor)) {
            validMove = false;
        }

        undoMove(start, mover, end, capture);

        teamTurn = currTurn;

        return validMove;
    }

    public void doTestMove(ChessMove move) {
        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = gameBoard.getPiece(start);
        ChessPiece capture = gameBoard.getPiece(end);

        if (piece != null && piece.getTeamColor() == teamTurn) {
            if (promo == null) {
                gameBoard.addPiece(end, piece);
            } else {
                gameBoard.addPiece(end, new ChessPiece(teamTurn, promo));
            }

            gameBoard.addPiece(start, null);

        }
    }

    public void undoMove(ChessPosition start, ChessPiece mover, ChessPosition end, ChessPiece capture) {
        gameBoard.addPiece(start, mover);
        gameBoard.addPiece(end, capture);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return teamTurn == chessGame.teamTurn && Objects.equals(gameBoard, chessGame.gameBoard) && Objects.equals(testBoard, chessGame.testBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, gameBoard, testBoard);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", gameBoard=" + gameBoard +
                ", testBoard=" + testBoard +
                '}';
    }
}
