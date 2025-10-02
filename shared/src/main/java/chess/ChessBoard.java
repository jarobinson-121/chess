package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.KING;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] boardGrid;
    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;

    public ChessBoard() {
        this.boardGrid = new ChessPiece[8][8];
        this.whiteKingPos = null;
        this.blackKingPos = null;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        boardGrid[position.getRow() - 1][position.getColumn() - 1] = piece;
        if (piece.getPieceType() == KING) {
            if (piece.getTeamColor() == WHITE) {
                whiteKingPos = position;
            } else {
                blackKingPos = position;
            }
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return boardGrid[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] pieceOrder = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardGrid[i][j] = null;
            }
        }

        for (int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(1, i), new ChessPiece(WHITE, pieceOrder[i - 1]));
            addPiece(new ChessPosition(2, i), new ChessPiece(WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, pieceOrder[i - 1]));
        }
    }

    public ChessPosition getKingPos(ChessGame.TeamColor color) {
        if (color == WHITE) {
            return whiteKingPos;
        } else {
            return blackKingPos;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(boardGrid, that.boardGrid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardGrid);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "boardGrid=" + Arrays.toString(boardGrid) +
                '}';
    }
}
