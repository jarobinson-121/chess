package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMoveCalculator extends BaseMovesAbstract {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new HashSet<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
//                if (i == 0 && j == 0) {
//                    continue;
//                }
                calculateMoves(board, pos, i, j, moves, false);
            }
        }
        return moves;
    }
}
