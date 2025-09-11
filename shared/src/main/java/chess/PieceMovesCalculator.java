package chess;

import java.util.Collection;

public interface PieceMovesCalculator {

    Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition myposition);
}
