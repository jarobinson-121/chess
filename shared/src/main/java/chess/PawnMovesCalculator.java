package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PawnMovesCalculator extends BaseMovesAbstract {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();

        int rightdiag = myPosition.getColumn() + 1;
        int leftDiag = myPosition.getColumn() - 1;

        ChessPiece pawn = board.getPiece(myPosition);

        ChessPiece.PieceType[] promoOpts = {
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT
        };

        if (pawn.getTeamColor() == WHITE) {
            int moveRow = myPosition.getRow() + 1;
            if (myPosition.getRow() == 2) {
                ChessPiece one = board.getPiece(new ChessPosition(3, myPosition.getColumn()));
                ChessPiece two = board.getPiece(new ChessPosition(4, myPosition.getColumn()));
                if (one == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(3, myPosition.getColumn()), null));
                    if (two == null) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(4, myPosition.getColumn()), null));
                    }
                }

            }
            if (moveRow <= 8) {
                ChessPiece frontPiece = board.getPiece(new ChessPosition(moveRow, myPosition.getColumn()));
                if (frontPiece == null) {
                    if (moveRow == 8) {
                        for (ChessPiece.PieceType option : promoOpts) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, myPosition.getColumn()), option));
                        }
                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, myPosition.getColumn()), null));
                    }

                }
                if (rightdiag <= 8) {
                    ChessPiece rightPiece = board.getPiece(new ChessPosition(moveRow, rightdiag));
                    if (rightPiece != null && rightPiece.getTeamColor() != WHITE) {
                        if (moveRow == 8) {
                            for (ChessPiece.PieceType option : promoOpts) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, rightdiag), option));
                            }
                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, rightdiag), null));
                        }

                    }
                }
                if (leftDiag >= 1) {
                    ChessPiece leftPiece = board.getPiece(new ChessPosition(moveRow, leftDiag));
                    if (leftPiece != null && leftPiece.getTeamColor() != WHITE) {
                        if (moveRow == 8) {
                            for (ChessPiece.PieceType option : promoOpts) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, leftDiag), option));
                            }
                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, leftDiag), null));
                        }
                    }
                }
            }

        }

        if (pawn.getTeamColor() == BLACK) {
            int moveRow = myPosition.getRow() - 1;
            if (myPosition.getRow() == 7) {
                ChessPiece one = board.getPiece(new ChessPosition(6, myPosition.getColumn()));
                ChessPiece two = board.getPiece(new ChessPosition(5, myPosition.getColumn()));
                if (one == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(6, myPosition.getColumn()), null));
                    if (two == null) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(5, myPosition.getColumn()), null));
                    }
                }

            }
            if (moveRow >= 1) {
                ChessPiece frontPiece = board.getPiece(new ChessPosition(moveRow, myPosition.getColumn()));
                if (frontPiece == null) {
                    if (moveRow == 1) {
                        for (ChessPiece.PieceType option : promoOpts) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, myPosition.getColumn()), option));
                        }
                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, myPosition.getColumn()), null));
                    }

                }
                if (rightdiag <= 8) {
                    ChessPiece rightPiece = board.getPiece(new ChessPosition(moveRow, rightdiag));
                    if (rightPiece != null && rightPiece.getTeamColor() != BLACK) {
                        if (moveRow == 1) {
                            for (ChessPiece.PieceType option : promoOpts) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, rightdiag), option));
                            }
                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, rightdiag), null));
                        }

                    }
                }
                if (leftDiag >= 1) {
                    ChessPiece leftPiece = board.getPiece(new ChessPosition(moveRow, leftDiag));
                    if (leftPiece != null && leftPiece.getTeamColor() != BLACK) {
                        if (moveRow == 1) {
                            for (ChessPiece.PieceType option : promoOpts) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, leftDiag), option));
                            }
                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, leftDiag), null));
                        }
                    }
                }
            }
        }

        return moves;
    }

}
