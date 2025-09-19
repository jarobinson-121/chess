package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator extends BaseMovesAbstract {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        calculateMoves(board, myPosition, 0, -1, moves, true);
        calculateMoves(board, myPosition, -1, 0, moves, true);
        calculateMoves(board, myPosition, 0, 1, moves, true);
        calculateMoves(board, myPosition, 1, 0, moves, true);
        return moves;
    }
}
