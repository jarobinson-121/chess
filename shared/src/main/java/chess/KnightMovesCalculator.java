package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow() + 1;
        int myCol = myPosition.getColumn() + 1;

        ChessPiece knight = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        // Up One, Right Two
        ChessPosition upORightT = new ChessPosition(myRow + 1, myCol + 2);
        ChessPiece upRPiece = null;
        if (upORightT.getRow() < 8 && upORightT.getColumn() < 8) {
            upRPiece = board.getPiece(upORightT);
            if((upRPiece == null || upRPiece.getTeamColor() != knight.getTeamColor()) ) {
                // Capture the piece and exit while noop
                moves.add(new ChessMove(myPosition, upORightT, null));
            }
        }


        // Up Two, Right One
        ChessPosition upTRightO = new ChessPosition(myRow + 2, myCol + 1);
        boolean upROBound = false;
        ChessPiece upRTPiece = null;
        if (upTRightO.getRow() < 8 && upTRightO.getColumn() < 8) {
            upROBound = true;
            upRTPiece = board.getPiece(upTRightO);
        }
        if( upROBound && (upRTPiece == null || upRTPiece.getTeamColor() != knight.getTeamColor()) ) {
            // Capture the piece and exit while noop
            moves.add(new ChessMove(myPosition, upTRightO, null));
        }

        // Down One, Right Two
        ChessPosition downORightT = new ChessPosition(myRow - 1, myCol + 2);
        boolean downRBound = false;
        ChessPiece downRPiece = null;
        if (downORightT.getRow() + 1 > 0 && downORightT.getColumn() < 8) {
            downRBound = true;
            downRPiece = board.getPiece(downORightT);
        }
        if( downRBound && (downRPiece == null || downRPiece.getTeamColor() != knight.getTeamColor()) ) {
                moves.add(new ChessMove(myPosition, downORightT, null));
        }

        // Down Two, Right One
        ChessPosition downTRightO = new ChessPosition(myRow - 2, myCol + 1);
        boolean downROBound = false;
        ChessPiece downROPiece = null;
        if (downTRightO.getRow() + 1 > 0 && downTRightO.getColumn() < 8) {
            downROBound = true;
            downROPiece = board.getPiece(downTRightO);
        }
        if( downROBound && (downROPiece == null || downROPiece.getTeamColor() != knight.getTeamColor()) ) {
            moves.add(new ChessMove(myPosition, downTRightO, null));
        }

        // Up One, Left Two
        ChessPosition upOLeftT = new ChessPosition(myRow + 1, myCol - 2);
        boolean upLBound = false;
        ChessPiece upLPiece = null;
        if( upOLeftT.getColumn() + 1 > 0  && upOLeftT.getRow() < 8) {
            upLBound = true;
            upLPiece = board.getPiece(upOLeftT);
        }
        if( upLBound && (upLPiece == null || upLPiece.getTeamColor() != knight.getTeamColor()) ) {
                moves.add(new ChessMove(myPosition, upOLeftT, null));
        }

        // Up Two, Left One
        ChessPosition upTLeftO = new ChessPosition(myRow + 2, myCol - 1);
        boolean upLOBound = false;
        ChessPiece upLOPiece = null;
        if( upTLeftO.getColumn() + 1 > 0  && upTLeftO.getRow() < 8) {
            upLOBound = true;
            upLOPiece = board.getPiece(upTLeftO);
        }
        if( upLOBound && (upLOPiece == null || upLOPiece.getTeamColor() != knight.getTeamColor()) ) {
            moves.add(new ChessMove(myPosition, upTLeftO, null));
        }

        // Down One, Left Two
        ChessPosition downOLeftT = new ChessPosition(myRow - 1, myCol - 2);
        boolean downLBound = false;
        ChessPiece downLPiece = null;
        if( downOLeftT.getColumn() + 1 > 0  && downOLeftT.getRow() + 1 > 0) {
            downLBound = true;
            downLPiece = board.getPiece(downOLeftT);
        }
        if( downLBound && (downLPiece == null || downLPiece.getTeamColor() != knight.getTeamColor()) ) {
            moves.add(new ChessMove(myPosition, downOLeftT, null));
        }

        // Down Two, Left One
        ChessPosition downTLeftO = new ChessPosition(myRow - 2, myCol - 1);
        boolean downLOBound = false;
        ChessPiece downLOPiece = null;
        if( downTLeftO.getColumn() + 1 > 0  && downTLeftO.getRow() + 1 > 0) {
            downLOBound = true;
            downLOPiece = board.getPiece(downTLeftO);
        }
        if( downLOBound && (downLOPiece == null || downLOPiece.getTeamColor() != knight.getTeamColor()) ) {
            moves.add(new ChessMove(myPosition, downTLeftO, null));
        }
        return moves;
    }
}
