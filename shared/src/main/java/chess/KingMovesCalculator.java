package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        int myRow = myPosition.getRow() + 1;
        int myCol = myPosition.getColumn() + 1;

        ChessPiece king = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        if(myRow < 8) {
            ChessPosition newPosition = new ChessPosition(myRow + 1, myCol);
            ChessPiece piece = board.getPiece(newPosition);

            System.out.println(board.getPiece(newPosition));
            System.out.println(board.getPiece(myPosition));

            if( piece == null || piece.getTeamColor() != king.getTeamColor() ) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }

        }

        // Decrease row by one
        if(myRow > 1) {
            ChessPosition newPosition = new ChessPosition(myRow - 1, myCol);
            ChessPiece piece = board.getPiece(newPosition);

            if( piece == null|| piece.getTeamColor() != king.getTeamColor() ) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }

        }

        if(myCol < 8) {
            ChessPosition newPosition = new ChessPosition(myRow, myCol + 1);
            ChessPiece piece = board.getPiece(newPosition);

            if( piece == null|| piece.getTeamColor() != king.getTeamColor() ) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(myCol > 1) {
            ChessPosition newPosition = new ChessPosition(myRow, myCol - 1);
            ChessPiece piece = board.getPiece(newPosition);

            if( piece == null|| piece.getTeamColor() != king.getTeamColor() ) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(myRow < 8 && myCol < 8) {
            ChessPosition newPosition = new ChessPosition(myRow + 1, myCol + 1);
            ChessPiece piece = board.getPiece(newPosition);

            if( piece == null|| piece.getTeamColor() != king.getTeamColor() ) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(myRow < 8 && myCol > 1) {
            ChessPosition newPosition = new ChessPosition(myRow + 1, myCol - 1);
            ChessPiece piece = board.getPiece(newPosition);

            if( piece == null|| piece.getTeamColor() != king.getTeamColor() ) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(myRow > 1 && myCol > 1) {
            ChessPosition newPosition = new ChessPosition(myRow - 1, myCol - 1);
            ChessPiece piece = board.getPiece(newPosition);

            if( piece == null|| piece.getTeamColor() != king.getTeamColor() ) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(myRow > 1 && myCol < 8) {
            ChessPosition newPosition = new ChessPosition(myRow - 1, myCol + 1);
            ChessPiece piece = board.getPiece(newPosition);

            if( piece == null|| piece.getTeamColor() != king.getTeamColor() ) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        return moves;

    }
}
