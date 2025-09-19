package chess;

import java.util.Collection;

public abstract class BaseMovesAbstract implements PieceMovesCalculator {

//    Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition) {
//
//    }

    protected void calculateMoves(ChessBoard board, ChessPosition pos, int rowInc, int colInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {

        int moveRow = pos.getRow() + rowInc;
        int moveCol = pos.getColumn() + colInc;

        if (moveRow > 8 || moveCol > 8 || moveRow < 1 || moveCol < 1) {
            return;
        }
        ChessPosition newSpot = new ChessPosition(pos.getRow() + rowInc, pos.getColumn() + colInc);
        if (board.getPiece(newSpot) == null) {
            moves.add(new ChessMove(pos, newSpot, null));
        } else if (board.getPiece(newSpot) != null) {
            ChessPiece currPiece = board.getPiece(pos);
            ChessPiece maybePiece = board.getPiece(newSpot);
            if (currPiece.getTeamColor() != maybePiece.getTeamColor()) {
                moves.add(new ChessMove(pos, newSpot, null));
            }
            allowDistance = false;
        }
        if (allowDistance) {
            calculateMoves(board, pos, rowInc + Integer.signum(rowInc), colInc + Integer.signum(colInc), moves, allowDistance);
        }

    }

}
