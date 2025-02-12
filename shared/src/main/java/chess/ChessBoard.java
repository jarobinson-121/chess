package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 * Use a matrix
 */
public class ChessBoard {

    private final ChessPiece[][] BoardGrid;

    public ChessBoard() {
        this.BoardGrid = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        BoardGrid[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return BoardGrid[position.getRow()][position.getColumn()];
    }

    public void setPiece(ChessPosition position, ChessPiece piece) {
        BoardGrid[position.getRow()][position.getColumn()] = piece;
    }

    public void clearPiece(ChessPosition position) {
        BoardGrid[position.getRow()][position.getColumn()] = null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        ChessPiece.PieceType[] pieceOrder = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

//      Sets whole board to null
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                BoardGrid[i][j] = null;
            }
        }

//      Adding pieces to the board
        for(int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, pieceOrder[i - 1]));
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, pieceOrder[i - 1]));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(BoardGrid, that.BoardGrid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(BoardGrid);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "BoardGrid=" + Arrays.deepToString(BoardGrid) +
                '}';
    }
}
