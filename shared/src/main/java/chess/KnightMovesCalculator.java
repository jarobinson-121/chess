package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class KnightMovesCalculator extends BaseMovesAbstract {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new HashSet<>();

        List<Integer> twos = List.of(-2, 2);
        List<Integer> ones = List.of(1, -1);

        for (Integer two : twos) {
            for (Integer one : ones) {
                calculateMoves(board, pos, one, two, moves, false);
                calculateMoves(board, pos, two, one, moves, false);
            }
        }
        return moves;
    }
}
