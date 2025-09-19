package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator extends BaseMovesAbstract {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        calculateMoves(board, myPosition, 0, -1, moves, false);
        calculateMoves(board, myPosition, -1, 0, moves, false);
        calculateMoves(board, myPosition, 0, 1, moves, false);
        calculateMoves(board, myPosition, 1, 0, moves, false);
        calculateMoves(board, myPosition, -1, -1, moves, false);
        calculateMoves(board, myPosition, 1, -1, moves, false);
        calculateMoves(board, myPosition, -1, 1, moves, false);
        calculateMoves(board, myPosition, 1, 1, moves, false);
        return moves;
    }
}
