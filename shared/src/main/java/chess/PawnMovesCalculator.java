package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.WHITE;

public class PawnMovesCalculator extends BaseMovesAbstract {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();

        ChessPiece.PieceType[] promoOpts = {
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT
        };

        ChessPiece pawn = board.getPiece(myPosition);

        int col = myPosition.getColumn();

        int rightDiag = col + 1;
        int leftDiag = col - 1;

        if (pawn != null) {
            int moveInc = (pawn.getTeamColor() == WHITE) ? +1 : -1;
            int startRow = (pawn.getTeamColor() == WHITE) ? 2 : 7;
            int promoRow = (pawn.getTeamColor() == WHITE) ? 8 : 1;

            int moveRow = myPosition.getRow() + moveInc;

            if (myPosition.getRow() == startRow) {
                int twoAhead = moveRow + moveInc;
                if (inBounds(moveRow, col) && inBounds(twoAhead, col)) {
                    ChessPiece one = board.getPiece(new ChessPosition(moveRow, col));
                    ChessPiece two = board.getPiece(new ChessPosition(twoAhead, col));
                    if (one == null && two == null) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(twoAhead, col), null));
                    }
                }
            }

            if (inBounds(moveRow, col)) {
                ChessPiece frontPiece = board.getPiece(new ChessPosition(moveRow, col));
                if (frontPiece == null) {
                    addMoves(moves, moveRow, promoRow, promoOpts, myPosition, col);

                }
                if (rightDiag <= 8) {
                    ChessPiece rightPiece = board.getPiece(new ChessPosition(moveRow, rightDiag));
                    if (rightPiece != null && rightPiece.getTeamColor() != pawn.getTeamColor()) {
                        addMoves(moves, moveRow, promoRow, promoOpts, myPosition, rightDiag);

                    }
                }
                if (leftDiag >= 1) {
                    ChessPiece leftPiece = board.getPiece(new ChessPosition(moveRow, leftDiag));
                    if (leftPiece != null && leftPiece.getTeamColor() != pawn.getTeamColor()) {
                        addMoves(moves, moveRow, promoRow, promoOpts, myPosition, leftDiag);
                    }
                }
            }
        }
        return moves;
    }

    private void addMoves(Collection<ChessMove> moves,
                          int moveRow,
                          int promoRow,
                          ChessPiece.PieceType[] promoOpts,
                          ChessPosition myPosition,
                          int diag) {
        if (moveRow == promoRow) {
            for (ChessPiece.PieceType option : promoOpts) {
                moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, diag), option));
            }
        } else {
            moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, diag), null));
        }
    }

    boolean inBounds(int row, int col) {
        if (col >= 1 && col <= 8 && row >= 1 && row <= 8) {
            return true;
        }
        return false;
    }

}
