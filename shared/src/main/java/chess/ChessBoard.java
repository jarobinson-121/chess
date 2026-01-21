package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] chessBoard;
    private ChessPosition whiteKing;
    private ChessPosition blackKing;

    public ChessBoard() {
        this.chessBoard = new ChessPiece[8][8];
        this.whiteKing = null;
        this.blackKing = null;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessBoard[position.getRow() - 1][position.getColumn() - 1] = piece;
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == WHITE) {
                whiteKing = position;
            } else if (piece.getTeamColor() == BLACK) {
                blackKing = position;
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
        return chessBoard[position.getRow() - 1][position.getColumn() - 1];
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
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoard[i][j] = null;
            }
        }

        for (int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(1, i), new ChessPiece(WHITE, pieceOrder[i - 1]));
            addPiece(new ChessPosition(2, i), new ChessPiece(WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(BLACK, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(8, i), new ChessPiece(BLACK, pieceOrder[i - 1]));
        }
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "chessBoard=" + Arrays.toString(chessBoard) +
                ", whiteKing=" + whiteKing +
                ", blackKing=" + blackKing +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
        return Objects.deepEquals(chessBoard, that.chessBoard)
                && Objects.equals(whiteKing, that.whiteKing)
                && Objects.equals(blackKing, that.blackKing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(chessBoard), whiteKing, blackKing);
    }
}
