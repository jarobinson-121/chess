package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow() + 1;
        int myCol = myPosition.getColumn() + 1;

        ChessPiece pawn = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();

        ChessPiece.PieceType[] promoOpts = ChessPiece.PieceType.values();

        if(pawn.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if(myRow == 2) {
                ChessPosition newPosition = new ChessPosition(myRow + 2, myCol);
                ChessPiece piece = board.getPiece(newPosition);

                if( piece == null ) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }

            }
            if(myRow < 8) {
                ChessPosition newPosition = new ChessPosition(myRow + 1, myCol);
                ChessPiece piece = board.getPiece(newPosition);

                if( piece == null ) {
                    if(newPosition.getRow() + 1 == 8) {
                        for(int i = 1; i < 5; i++) {
                            moves.add(new ChessMove(myPosition, newPosition, promoOpts[i]));
                        }
                    }
                    else {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }

                ChessPosition RPieceSpot = new ChessPosition(myRow + 1, myCol + 1);
                if( RPieceSpot.getColumn() < 8) {
                    ChessPiece RPiece = board.getPiece(RPieceSpot);
                    if( RPiece != null && RPiece.getTeamColor() != pawn.getTeamColor() ) {
                        if(RPieceSpot.getRow() + 1 == 8) {
                            for(int i = 1; i < 5; i++) {
                                moves.add(new ChessMove(myPosition, RPieceSpot, promoOpts[i]));
                            }
                        }
                        else {
                            moves.add(new ChessMove(myPosition, RPieceSpot, null));
                        }
                    }
                }
                ChessPosition LPieceSpot = new ChessPosition(myRow + 1, myCol - 1);
                if( LPieceSpot.getColumn() + 1 > 0) {
                    ChessPiece LPiece = board.getPiece(LPieceSpot);
                    if( LPiece != null && LPiece.getTeamColor() != pawn.getTeamColor() ) {
                        if(LPieceSpot.getRow() + 1 == 8) {
                            for(int i = 1; i < 5; i++) {
                                moves.add(new ChessMove(myPosition, LPieceSpot, promoOpts[i]));
                            }
                        }
                        else {
                            moves.add(new ChessMove(myPosition, LPieceSpot, null));
                        }
                    }
                }
            }



        }

        if(pawn.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (myRow == 7) {
                ChessPosition newPosition = new ChessPosition(myRow - 2, myCol);
                ChessPosition middlePosition = new ChessPosition(myRow - 1, myCol );
                ChessPiece middlePiece = board.getPiece(middlePosition);
                ChessPiece piece = board.getPiece(newPosition);

                if (piece == null && middlePiece == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }

            }
            if (myRow > 1) {
                ChessPosition newPosition = new ChessPosition(myRow - 1, myCol);
                ChessPiece piece = board.getPiece(newPosition);

                if (piece == null) {
                    if(newPosition.getRow() + 1 == 1) {
                        for(int i = 1; i < 5; i++) {
                            moves.add(new ChessMove(myPosition, newPosition, promoOpts[i]));
                        }
                    }
                    else {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }

                ChessPosition RPieceSpot = new ChessPosition(myRow - 1, myCol + 1);
                if (RPieceSpot.getColumn() + 1 < 8) {
                    ChessPiece RPiece = board.getPiece(RPieceSpot);
                    if (RPiece != null && RPiece.getTeamColor() != pawn.getTeamColor()) {
                        if(RPieceSpot.getRow() == 0) {
                            for(int i = 1; i < 5; i++) {
                                moves.add(new ChessMove(myPosition, RPieceSpot, promoOpts[i]));
                            }
                        }
                        else {
                            moves.add(new ChessMove(myPosition, RPieceSpot, null));
                        }
                    }
                }

                ChessPosition LPieceSpot = new ChessPosition(myRow - 1, myCol - 1);
                if (LPieceSpot.getColumn() + 1 > 0) {
                    ChessPiece LPiece = board.getPiece(LPieceSpot);
                    if (LPiece != null && LPiece.getTeamColor() != pawn.getTeamColor()) {
                        if(LPieceSpot.getRow() == 0) {
                            for(int i = 1; i < 5; i++) {
                                moves.add(new ChessMove(myPosition, LPieceSpot, promoOpts[i]));
                            }
                        }
                        else {
                            moves.add(new ChessMove(myPosition, LPieceSpot, null));
                        }
                    }
                }
            }


//            if (myRow > 1) {
//                ChessPosition newPosition = new ChessPosition(myRow - 1, myCol -1);
//                ChessPiece piece = board.getPiece(newPosition);
//
//                if (piece == null) {
//                    if(newPosition.getRow() == 0) {
//                        for(int i = 1; i < 5; i++) {
//                            moves.add(new ChessMove(myPosition, newPosition, promoOpts[i]));
//                        }
//                    }
//                    else {
//                        moves.add(new ChessMove(myPosition, newPosition, null));
//                    }
//                }
//
//            }
        }


        return moves;
    }
}
