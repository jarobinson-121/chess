package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow() + 1;
        int myColumn = myPosition.getColumn() + 1;
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece queen = board.getPiece(myPosition);

        ChessPosition newPosition = new ChessPosition(myRow + 1, myColumn);
        while(newPosition.getRow() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(newPosition.getRow() + 2, myColumn);
            }
            else if (newPiece.getTeamColor() == queen.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
        }

        newPosition = new ChessPosition(myRow - 1, myColumn);
        while(newPosition.getRow() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(newPosition.getRow(), myColumn);
            }
            else if (newPiece.getTeamColor() == queen.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
        }

        newPosition = new ChessPosition(myRow, myColumn + 1);
        while(newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(myRow, newPosition.getColumn() + 2);
            }
            else if (newPiece.getTeamColor() == queen.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
        }

        newPosition = new ChessPosition(myRow, myColumn - 1);
        while(newPosition.getColumn() >= 0) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(myRow, newPosition.getColumn());
            }
            else if (newPiece.getTeamColor() == queen.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
        }

        // Up Right
        newPosition = new ChessPosition(myRow + 1, myColumn + 1);
        while(newPosition.getRow() < 8 && newPosition.getColumn() < 8) {
            ChessPiece newPiece = board.getPiece(newPosition);
            if(newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(newPosition.getRow() + 2, newPosition.getColumn() + 2);
            }
            else if (newPiece.getTeamColor() == queen.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != queen.getTeamColor()) {
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
            else if (newPiece.getTeamColor() == queen.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != queen.getTeamColor()) {
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
            else if (newPiece.getTeamColor() == queen.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != queen.getTeamColor()) {
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
            else if (newPiece.getTeamColor() == queen.getTeamColor()) {
                break;
            }
            else if(newPiece.getTeamColor() != queen.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
        }

        return moves;
    }
}
