package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int myRow = myPosition.getRow() + 1;
        int myColumn = myPosition.getColumn() + 1;
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece pawn = board.getPiece(myPosition);


        if (pawn.getTeamColor() == ChessGame.TeamColor.BLACK) {

            ChessPiece.PieceType[] promoOpts = {
                    ChessPiece.PieceType.QUEEN,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.KNIGHT,
                    ChessPiece.PieceType.ROOK
            };

            ChessPosition right = new ChessPosition(myRow - 1, myColumn + 1);
            ChessPosition left = new ChessPosition(myRow - 1, myColumn - 1);

            if(right.getRow() >= 0 && right.getColumn() + 1 < 8) {
                ChessPiece rPiece = board.getPiece(right);
                if( rPiece != null && rPiece.getTeamColor() != ChessGame.TeamColor.BLACK) {
                    if(right.getRow() + 1 == 1) {
                        for ( int i = 0; i < 4; i++) {
                            moves.add(new ChessMove(myPosition, right, promoOpts[i]));
                        }
                    } else {
                        moves.add(new ChessMove(myPosition, right, null));
                    }

                }
            }

            if(left.getRow() >= 0 && left.getColumn() >= 0) {
                ChessPiece lPiece = board.getPiece(left);
                if( lPiece != null && lPiece.getTeamColor() != ChessGame.TeamColor.BLACK) {
                    if(left.getRow() + 1 == 1) {
                        for ( int i = 0; i < 4; i++) {
                            moves.add(new ChessMove(myPosition, left, promoOpts[i]));
                        }
                    } else {
                        moves.add(new ChessMove(myPosition, left, null));
                    }

                }
            }

            if (myRow == 7) {
                ChessPosition one = new ChessPosition(myRow - 1, myColumn);
                ChessPosition two = new ChessPosition(myRow - 2, myColumn);
                if(board.getPiece(one) == null) {
                    moves.add(new ChessMove(myPosition, one, null));
                    if(board.getPiece(two) == null) {
                        moves.add(new ChessMove(myPosition, two, null));
                    }
                }
            } else if (myPosition.getRow() > 1) {
                ChessPosition one = new ChessPosition(myRow - 1, myColumn);
                if(board.getPiece(one) == null) {
                    moves.add(new ChessMove(myPosition, one, null));
                }
            } else if (myPosition.getRow() == 1 && board.getPiece(new ChessPosition(myRow - 1, myColumn)) == null) {
                for ( int i = 0; i < 4; i++) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(1, myColumn), promoOpts[i]));
                }
            }
        }

        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE) {

            ChessPiece.PieceType[] promoOpts = {
                    ChessPiece.PieceType.QUEEN,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.KNIGHT,
                    ChessPiece.PieceType.ROOK
            };

            ChessPosition right = new ChessPosition(myRow + 1, myColumn + 1);
            ChessPosition left = new ChessPosition(myRow + 1, myColumn - 1);

            if(right.getRow() < 8 && right.getColumn() + 1 < 8) {
                ChessPiece rPiece = board.getPiece(right);
                if( rPiece != null && rPiece.getTeamColor() != ChessGame.TeamColor.WHITE) {
                    if(right.getRow() + 1 == 8) {
                        for ( int i = 0; i < 4; i++) {
                            moves.add(new ChessMove(myPosition, right, promoOpts[i]));
                        }
                    } else {
                        moves.add(new ChessMove(myPosition, right, null));
                    }

                }
            }

            if(left.getRow() < 8 && left.getColumn() > 0) {
                ChessPiece lPiece = board.getPiece(left);
                if( lPiece != null && lPiece.getTeamColor() != ChessGame.TeamColor.WHITE) {
                    if(left.getRow() + 1 == 8) {
                        for ( int i = 0; i < 4; i++) {
                            moves.add(new ChessMove(myPosition, left, promoOpts[i]));
                        }
                    } else {
                        moves.add(new ChessMove(myPosition, left, null));
                    }

                }
            }

            if (myRow == 2) {
                ChessPosition one = new ChessPosition(myRow + 1, myColumn);
                ChessPosition two = new ChessPosition(myRow + 2, myColumn);
                if(board.getPiece(one) == null) {
                    moves.add(new ChessMove(myPosition, one, null));
                    if(board.getPiece(two) == null) {
                        moves.add(new ChessMove(myPosition, two, null));
                    }
                }
            } else if (myPosition.getRow() + 2 < 8) {
                ChessPosition one = new ChessPosition(myRow + 1, myColumn);
                if(board.getPiece(one) == null) {
                    moves.add(new ChessMove(myPosition, one, null));
                }
            } else if (myPosition.getRow() + 2 == 8 && board.getPiece(new ChessPosition(myRow + 1, myColumn)) == null) {
                for ( int i = 0; i < 4; i++) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(8, myColumn), promoOpts[i]));
                }
            }
        }


        return moves;
    }
}
