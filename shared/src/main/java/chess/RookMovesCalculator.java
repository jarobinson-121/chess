package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class RookMovesCalculator extends BaseMovesAbstract {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new HashSet<>();

        for (int i = -1; i < 2; i++) {
            calculateMoves(board, pos, i, 0, moves, true);
            calculateMoves(board, pos, 0, i, moves, true);
        }
        return moves;
    }
}
