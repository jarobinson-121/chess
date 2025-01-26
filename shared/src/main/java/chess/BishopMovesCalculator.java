package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow() + 1;
        int myCol = myPosition.getColumn() + 1;

        ChessPiece bishop = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        ChessPosition upRight = new ChessPosition(myRow + 1, myCol + 1);
        boolean upRBound = false;
        ChessPiece upRPiece = null;
        if (upRight.getRow() < 8 && upRight.getColumn() < 8) {
            upRBound = true;
            upRPiece = board.getPiece(upRight);
        }
        while( upRBound && (upRight.getRow() < 8 && upRight.getColumn() < 8) && (upRPiece == null || upRPiece.getTeamColor() != bishop.getTeamColor()) ) {

            // Capture the piece and exit while noop
            if(upRPiece != null && upRPiece.getTeamColor() != bishop.getTeamColor()) {
                moves.add(new ChessMove(myPosition, upRight, null));
                break;
            }

            moves.add(new ChessMove(myPosition, upRight, null));

            upRight = new ChessPosition(upRight.getRow() + 2, upRight.getColumn() + 2);

            if(upRight.getRow() < 8 && upRight.getColumn() < 8) {
                upRPiece = board.getPiece(upRight);
            }
        }

        ChessPosition downRight = new ChessPosition(myRow - 1, myCol + 1);
        boolean downRBound = false;
        ChessPiece downRPiece = null;
        if (downRight.getRow() + 1 > 0 && downRight.getColumn() + 1 < 8) {
            downRBound = true;
            downRPiece = board.getPiece(downRight);
        }
        while( downRBound && (downRight.getRow() + 1 > 0 && downRight.getColumn() < 8) && (downRPiece == null || downRPiece.getTeamColor() != bishop.getTeamColor()) ) {

            if(downRPiece != null && downRPiece.getTeamColor() != bishop.getTeamColor()) {
                moves.add(new ChessMove(myPosition, downRight, null));
                break;
            }

            moves.add(new ChessMove(myPosition, downRight, null));

            downRight = new ChessPosition(downRight.getRow(), downRight.getColumn() + 2);

            if(downRight.getRow() + 1 > 0 && downRight.getColumn() + 1 < 8) {
                downRPiece = board.getPiece(downRight);
            }
        }


        ChessPosition upLeft = new ChessPosition(myRow + 1, myCol - 1);
        boolean upLBound = false;
        ChessPiece upLPiece = null;
        if( upLeft.getColumn() + 1 > 0  && upLeft.getRow() + 1 < 8) {
            upLBound = true;
            upLPiece = board.getPiece(upLeft);
        }
        while( upLBound && (upLeft.getColumn() + 1 > 0 && upLeft.getRow() < 8) && (upLPiece == null || upLPiece.getTeamColor() != bishop.getTeamColor()) ) {

            if(upLPiece != null && upLPiece.getTeamColor() != bishop.getTeamColor()) {
                moves.add(new ChessMove(myPosition, upLeft, null));
                break;
            }

            moves.add(new ChessMove(myPosition, upLeft, null));

            upLeft = new ChessPosition(upLeft.getRow() + 2, upLeft.getColumn());
            if(upLeft.getColumn() + 1 > 0 && upLeft.getRow() + 1 < 8) {
                upLPiece = board.getPiece(upLeft);
            }
        }

        ChessPosition downLeft = new ChessPosition(myRow - 1, myCol - 1);
        boolean downLBound = false;
        ChessPiece downLPiece = null;
        if(downLeft.getColumn() + 1 > 0  && downLeft.getRow() + 1 > 0) {
            downLBound = true;
            downLPiece = board.getPiece(downLeft);
        }
        while( downLBound && (downLeft.getColumn() + 1 > 0 && downLeft.getRow() + 1 > 0) && (downLPiece == null || downLPiece.getTeamColor() != bishop.getTeamColor()) ) {

            if(downLPiece != null && downLPiece.getTeamColor() != bishop.getTeamColor()) {
                moves.add(new ChessMove(myPosition, downLeft, null));
                break;
            }

            moves.add(new ChessMove(myPosition, downLeft, null));

            downLeft = new ChessPosition(downLeft.getRow(), downLeft.getColumn());
            if(downLeft.getColumn() + 1 > 0 && downLeft.getRow() + 1 > 0) {
                downLPiece = board.getPiece(downLeft);
            }
        }

        return moves;
    }
}

