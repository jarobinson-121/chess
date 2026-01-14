package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class BishopMovesCalculator extends BaseMovesAbstract {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new HashSet<>();
        List<Integer> incs = List.of(-1, 1);
        List<Integer> invertedIncs = List.of(1, -1);

        for (Integer inc : incs) {
            for (Integer invert : invertedIncs) {
                calculateMoves(board, pos, inc, invert, moves, true);
            }
        }
        return moves;
    }
}
