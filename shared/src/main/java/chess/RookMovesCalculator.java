package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        int myRow = myPosition.getRow() + 1;
        int myCol = myPosition.getColumn() + 1;

        ChessPiece rook = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        ChessPosition upRow = new ChessPosition(myRow + 1, myCol);
        boolean upBound = false;
        ChessPiece upPiece = null;
        if (upRow.getRow() < 8) {
            upBound = true;
            upPiece = board.getPiece(upRow);
        }
        while( upBound && upRow.getRow() < 8 && (upPiece == null || upPiece.getTeamColor() != rook.getTeamColor()) ) {

            // Capture the piece and exit while noop
            if(upPiece != null && upPiece.getTeamColor() != rook.getTeamColor()) {
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
        while( downBound && downRow.getRow() + 1 > 0 && (downPiece == null || downPiece.getTeamColor() != rook.getTeamColor()) ) {

            if(downPiece != null && downPiece.getTeamColor() != rook.getTeamColor()) {
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
        while( leftBound && leftCol.getColumn() + 1 > 0 && (leftPiece == null || leftPiece.getTeamColor() != rook.getTeamColor()) ) {

            if(leftPiece != null && leftPiece.getTeamColor() != rook.getTeamColor()) {
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
        while( rightBound && rightCol.getColumn() < 8 && (rightPiece == null || rightPiece.getTeamColor() != rook.getTeamColor()) ) {

            if(rightPiece != null && rightPiece.getTeamColor() != rook.getTeamColor()) {
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
