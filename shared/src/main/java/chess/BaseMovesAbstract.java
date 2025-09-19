package chess;

import java.util.Collection;

public abstract class BaseMovesAbstract implements PieceMovesCalculator {

//    Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition) {
//
//    }

    protected void calculateMoves(ChessBoard board, ChessPosition pos, int rowInc, int colInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {

        ChessPosition newSpot = new ChessPosition(pos.getRow() + rowInc, pos.getColumn() + colInc);
        if (newSpot.getRow() > 7 || newSpot.getColumn() > 7) {
            //TODO: possibly need to set allowDistance to false
            return;
        } else if (board.getPiece(newSpot) != null) {
            ChessPiece currPiece = board.getPiece(pos);
            ChessPiece maybePiece = board.getPiece(newSpot);
            if (currPiece.getTeamColor() != maybePiece.getTeamColor()) {
                moves.add(new ChessMove(pos, newSpot, null));
            }
            if (allowDistance == true) {
                calculateMoves(board, newSpot, rowInc, colInc, moves, allowDistance);
            }
        }

    }

}
