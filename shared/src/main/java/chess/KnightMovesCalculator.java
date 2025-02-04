package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow() + 1;
        int myColumn = myPosition.getColumn() + 1;
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece king = board.getPiece(myPosition);

        // Up One, Right Two
        ChessPosition newPosition = new ChessPosition(myRow + 1, myColumn + 2);
        if (newPosition.getRow() < 8 && newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Up Two, Right One
        newPosition = new ChessPosition(myRow + 2, myColumn + 1);
        if (newPosition.getRow() < 8 && newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Up One, Left Two
        newPosition = new ChessPosition(myRow + 1, myColumn - 2);
        if (newPosition.getRow() < 8 && newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Up Two, Left One
        newPosition = new ChessPosition(myRow + 2, myColumn - 1);
        if (newPosition.getRow() < 8 && newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Down One, Right Two
        newPosition = new ChessPosition(myRow - 1, myColumn + 2);
        if (newPosition.getRow() >= 0 && newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // down Two, Right One
        newPosition = new ChessPosition(myRow - 2, myColumn + 1);
        if (newPosition.getRow() >= 0 && newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Down One, Left Two
        newPosition = new ChessPosition(myRow - 1, myColumn - 2);
        if (newPosition.getRow() >= 0 && newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        // Down Two, Left One
        newPosition = new ChessPosition(myRow - 2, myColumn - 1);
        if (newPosition.getRow() >= 0 && newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece == null || newPiece.getTeamColor() != king.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }



        return moves;
    }
}
