package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends BaseMovesAbstract {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        calculateMoves(board, myPosition, -1, -1, moves, true);
        calculateMoves(board, myPosition, 1, -1, moves, true);
        calculateMoves(board, myPosition, -1, 1, moves, true);
        calculateMoves(board, myPosition, 1, 1, moves, true);
        return moves;
    }
}
