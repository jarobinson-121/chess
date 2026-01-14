package chess;

import java.util.Collection;

public abstract class BaseMovesAbstract implements PieceMovesCalculator {

    void calculateMoves(ChessBoard board, ChessPosition pos, int rowInc, int colInc,
                        Collection<ChessMove> moves, boolean allowMult) {
        int moveRow = pos.getRow() + rowInc;
        int moveCol = pos.getColumn() + colInc;

        if (moveRow > 8 || moveCol > 8 || moveRow < 1 || moveCol < 1) {
            return;
        }
        ChessPosition movePos = new ChessPosition(moveRow, moveCol);
        if (movePos == pos) {
            return;
        }
        if (board.getPiece(movePos) == null) {
            moves.add(new ChessMove(pos, movePos, null));
        } else {
            ChessPiece mover = board.getPiece(pos);
            ChessPiece obstacle = board.getPiece(movePos);
            if (mover.getTeamColor() != obstacle.getTeamColor()) {
                moves.add(new ChessMove(pos, movePos, null));
            }
            allowMult = false;
        }
        if (allowMult) {
            calculateMoves(board, pos, addToInc(rowInc), addToInc(colInc), moves, allowMult);
        }
    }

    private int addToInc(int incVal) {
        return incVal + Integer.signum(incVal);
    }
}
