package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends BaseMovesAbstract {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new HashSet<>();

        ChessPiece.PieceType[] promoOpts = {
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP
        };

        ChessPiece mover = board.getPiece(pos);
        ChessGame.TeamColor moverColor = mover.getTeamColor();
        int vertInc = (moverColor == ChessGame.TeamColor.WHITE) ? +1 : -1;
        int startRow = (moverColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promoRow = (moverColor == ChessGame.TeamColor.WHITE) ? 8 : 1;
        int moveRow = pos.getRow() + vertInc;
        int col = pos.getColumn();

        int rightDiag = col + 1;
        int leftDiag = col - 1;

        if (pos.getRow() == startRow) {
            int twoForward = moveRow + vertInc;
            if (inBounds(moveRow, col) && inBounds(twoForward, col)) {
                ChessPiece one = board.getPiece(new ChessPosition(moveRow, col));
                ChessPiece two = board.getPiece(new ChessPosition(twoForward, col));
                if (one == null && two == null) {
                    moves.add(new ChessMove(pos, new ChessPosition(twoForward, col), null));
                }
            }
        }

        if (inBounds(moveRow, col)) {
            ChessPiece front = board.getPiece(new ChessPosition(moveRow, col));
            if (front == null) {
                addMove(moves, moveRow, promoRow, col, pos, promoOpts);
            }
            if (rightDiag <= 8) {
                ChessPiece rightCap = board.getPiece(new ChessPosition(moveRow, rightDiag));
                if (rightCap != null && rightCap.getTeamColor() != mover.getTeamColor()) {
                    addMove(moves, moveRow, promoRow, rightDiag, pos, promoOpts);
                }
            }
            if (leftDiag >= 1) {
                ChessPiece leftCap = board.getPiece(new ChessPosition(moveRow, leftDiag));
                if (leftCap != null && leftCap.getTeamColor() != mover.getTeamColor()) {
                    addMove(moves, moveRow, promoRow, leftDiag, pos, promoOpts);
                }
            }
        }
        return moves;
    }

    private void addMove(Collection<ChessMove> moves,
                         int moveRow,
                         int promoRow,
                         int col,
                         ChessPosition pos,
                         ChessPiece.PieceType[] promoOpts) {
        if (moveRow == promoRow) {
            for (ChessPiece.PieceType option : promoOpts) {
                moves.add(new ChessMove(pos, new ChessPosition(moveRow, col), option));
            }
        } else {
            moves.add(new ChessMove(pos, new ChessPosition(moveRow, col), null));
        }
    }

    private boolean inBounds(int row, int col) {
        if (row < 1 && row > 8 && col < 1 && col > 8) {
            return false;
        }
        return true;
    }

}
