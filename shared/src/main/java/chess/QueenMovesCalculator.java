package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow() + 1;
        int myCol = myPosition.getColumn() + 1;

        ChessPiece queen = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        ChessPosition upRight = new ChessPosition(myRow + 1, myCol + 1);
        boolean upRBound = false;
        ChessPiece upRPiece = null;
        if (upRight.getRow() < 8 && upRight.getColumn() < 8) {
            upRBound = true;
            upRPiece = board.getPiece(upRight);
        }
        while( upRBound && (upRight.getRow() < 8 && upRight.getColumn() < 8) && (upRPiece == null || upRPiece.getTeamColor() != queen.getTeamColor()) ) {

            // Capture the piece and exit while noop
            if(upRPiece != null && upRPiece.getTeamColor() != queen.getTeamColor()) {
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
        if (downRight.getRow() + 1 > 0 && downRight.getColumn() < 8) {
            downRBound = true;
            downRPiece = board.getPiece(downRight);
        }
        while( downRBound && (downRight.getRow() + 1 > 0 && downRight.getColumn() < 8) && (downRPiece == null || downRPiece.getTeamColor() != queen.getTeamColor()) ) {

            if(downRPiece != null && downRPiece.getTeamColor() != queen.getTeamColor()) {
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
        if( upLeft.getColumn() + 1 > 0  && upLeft.getRow() < 8) {
            upLBound = true;
            upLPiece = board.getPiece(upLeft);
        }
        while( upLBound && (upLeft.getColumn() + 1 > 0 && upLeft.getRow() < 8) && (upLPiece == null || upLPiece.getTeamColor() != queen.getTeamColor()) ) {

            if(upLPiece != null && upLPiece.getTeamColor() != queen.getTeamColor()) {
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
        while( downLBound && (downLeft.getColumn() + 1 > 0 && downLeft.getRow() + 1 > 0) && (downLPiece == null || downLPiece.getTeamColor() != queen.getTeamColor()) ) {

            if(downLPiece != null && downLPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, downLeft, null));
                break;
            }

            moves.add(new ChessMove(myPosition, downLeft, null));

            downLeft = new ChessPosition(downLeft.getRow(), downLeft.getColumn());
            if(downLeft.getColumn() + 1 > 0 && downLeft.getRow() + 1 > 0) {
                downLPiece = board.getPiece(downLeft);
            }
        }

        ChessPosition upRow = new ChessPosition(myRow + 1, myCol);
        boolean upBound = false;
        ChessPiece upPiece = null;
        if (upRow.getRow() < 8) {
            upBound = true;
            upPiece = board.getPiece(upRow);
        }
        while( upBound && upRow.getRow() < 8 && (upPiece == null || upPiece.getTeamColor() != queen.getTeamColor()) ) {

            // Capture the piece and exit while noop
            if(upPiece != null && upPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, upRow, null));
                break;
            }

            moves.add(new ChessMove(myPosition, upRow, null));

            upRow = new ChessPosition(upRow.getRow() + 2, myCol);

            if(upRow.getRow() < 8) {
                upPiece = board.getPiece(upRow);
            }
        }

        ChessPosition downRow = new ChessPosition(myRow - 1, myCol);
        boolean downBound = false;
        ChessPiece downPiece = null;
        if (downRow.getRow() + 1 > 0) {
            downBound = true;
            downPiece = board.getPiece(downRow);
        }
        while( downBound && downRow.getRow() + 1 > 0 && (downPiece == null || downPiece.getTeamColor() != queen.getTeamColor()) ) {

            if(downPiece != null && downPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, downRow, null));
                break;
            }

            moves.add(new ChessMove(myPosition, downRow, null));

            downRow = new ChessPosition(downRow.getRow(), myCol);

            if(downRow.getRow() + 1 > 0) {
                downPiece = board.getPiece(downRow);
            }
        }


        ChessPosition leftCol = new ChessPosition(myRow, myCol - 1);
        boolean leftBound = false;
        ChessPiece leftPiece = null;
        if( leftCol.getColumn() >= 0 ) {
            leftBound = true;
            leftPiece = board.getPiece(leftCol);
        }
        while( leftBound && leftCol.getColumn() + 1 > 0 && (leftPiece == null || leftPiece.getTeamColor() != queen.getTeamColor()) ) {

            if(leftPiece != null && leftPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, leftCol, null));
                break;
            }

            moves.add(new ChessMove(myPosition, leftCol, null));

            leftCol = new ChessPosition(myRow, leftCol.getColumn());
            if(leftCol.getColumn() >= 0) {
                leftPiece = board.getPiece(leftCol);
            }
        }

        ChessPosition rightCol = new ChessPosition(myRow, myCol + 1);
        boolean rightBound = false;
        ChessPiece rightPiece = null;
        if(rightCol.getColumn() < 8) {
            rightBound = true;
            rightPiece = board.getPiece(rightCol);
        }
        while( rightBound && rightCol.getColumn() < 8 && (rightPiece == null || rightPiece.getTeamColor() != queen.getTeamColor()) ) {

            if(rightPiece != null && rightPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, rightCol, null));
                break;
            }

            moves.add(new ChessMove(myPosition, rightCol, null));

            rightCol = new ChessPosition(myRow, rightCol.getColumn() + 2);
            if(rightCol.getColumn() < 8) {
                rightPiece = board.getPiece(rightCol);
            }
        }

        return moves;
    }
}
