package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow() + 1;
        int myColumn = myPosition.getColumn() + 1;
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece king = board.getPiece(myPosition);

        ChessPosition newPosition = new ChessPosition(myRow + 1, myColumn);
        if(newPosition.getRow() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        newPosition = new ChessPosition(myRow - 1, myColumn);
        if(newPosition.getRow() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        newPosition = new ChessPosition(myRow, myColumn + 1);
        if(newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        newPosition = new ChessPosition(myRow, myColumn - 1);
        if(newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Up Right
        newPosition = new ChessPosition(myRow + 1, myColumn + 1);
        if(newPosition.getRow() < 8 && newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Down Right
        newPosition = new ChessPosition(myRow - 1, myColumn + 1);
        if(newPosition.getRow() >= 0 && newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Up Left
        newPosition = new ChessPosition(myRow + 1, myColumn - 1);
        if(newPosition.getRow() < 8 && newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Down Left
        newPosition = new ChessPosition(myRow - 1, myColumn - 1);
        if(newPosition.getRow() >= 0 && newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        return moves;
    }
}
