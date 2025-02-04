package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow() + 1;
        int myColumn = myPosition.getColumn() + 1;
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece bishop = board.getPiece(myPosition);

        // Up Right
        ChessPosition newPosition = new ChessPosition(myRow + 1, myColumn + 1);
        while(newPosition.getRow() < 8 && newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(newPosition.getRow() + 2, newPosition.getColumn() + 2);
            }
            else if (newPiece.getTeamColor() == bishop.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != bishop.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
        }

        // Down Right
        newPosition = new ChessPosition(myRow - 1, myColumn + 1);
        while(newPosition.getRow() >= 0 && newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(newPosition.getRow(), newPosition.getColumn() + 2);
            }
            else if (newPiece.getTeamColor() == bishop.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != bishop.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
        }

        // Up Left
        newPosition = new ChessPosition(myRow + 1, myColumn - 1);
        while(newPosition.getRow() < 8 && newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(newPosition.getRow() + 2, newPosition.getColumn());
            }
            else if (newPiece.getTeamColor() == bishop.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != bishop.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
        }

        // Down Left
        newPosition = new ChessPosition(myRow - 1, myColumn - 1);
        while(newPosition.getRow() >= 0 && newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(newPosition.getRow(), newPosition.getColumn());
            }
            else if (newPiece.getTeamColor() == bishop.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != bishop.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
        }

        return moves;
    }
}
