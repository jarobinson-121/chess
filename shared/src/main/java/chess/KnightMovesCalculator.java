package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends BaseMovesAbstract {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        //left 2, down 1
        calculateMoves(board, myPosition, -2, -1, moves, false);
        // left 1, down 2
        calculateMoves(board, myPosition, -1, -2, moves, false);
        //right 1, down 2
        calculateMoves(board, myPosition, 1, -2, moves, false);
        //right 2, down 1
        calculateMoves(board, myPosition, 2, -1, moves, false);
        //right 2, up 1
        calculateMoves(board, myPosition, 2, 1, moves, false);
        //right 1, up 2
        calculateMoves(board, myPosition, 1, 2, moves, false);
        //left 2, up 1
        calculateMoves(board, myPosition, -2, 1, moves, false);
        //left 1, up 2
        calculateMoves(board, myPosition, -1, 2, moves, false);
        return moves;
    }
}
