package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class DrawBoard {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_HEIGHT_IN_CHARS = 3;
    private static final int SQUARE_WIDTH_IN_CHARS = 7;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    private final String playerColor;
    private final ChessGame game;
    private final ChessBoard board;

    // Padded characters.
    private static final String EMPTY = " ";

    public DrawBoard(String playerColor, ChessGame game) {
        this.playerColor = playerColor;
        this.game = game;
        this.board = game.getBoard();
    }

    public void main(String playerColor) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        String[] whiteHeaders = {"A", "B", "C", "D", "E", "F", "G", "H"};
        String[] blackHeaders = {"H", "G", "F", "E", "D", "C", "B", "A"};

        String[] whiteBorders = {"1", "2", "3", "4", "5", "6", "7", "8"};
        String[] blackBorders = {"8", "7", "6", "5", "4", "3", "2", "1"};

        out.print(ERASE_SCREEN);

        String[] headers = (playerColor.equals("white")) ? whiteHeaders : blackHeaders;
        String[] columns = (playerColor.equals("white")) ? whiteBorders : blackBorders;

        drawHeaders(out, headers);

        drawChessBoard(out, columns, playerColor, board);

        drawHeaders(out, headers);

        out.print(RESET_BG_COLOR);
        out.println();

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private void drawHeaders(PrintStream out, String[] headers) {

        setBlack(out);
        out.print(EMPTY.repeat(4));

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }
        
        out.print(EMPTY.repeat(3));
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_WIDTH_IN_CHARS / 2;
        int suffixLength = SQUARE_WIDTH_IN_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String headers) {
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(headers);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out, String[] columns, String playerColor, ChessBoard board) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, playerColor, columns, boardRow, board);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out,
                                         String color,
                                         String[] columns,
                                         int boardRow,
                                         ChessBoard board) {

        for (int squareRow = 0; squareRow < SQUARE_HEIGHT_IN_CHARS; ++squareRow) {
            boolean printSymbol = (squareRow == SQUARE_HEIGHT_IN_CHARS / 2) ? true : false;

            addColumnHeader(out, columns, printSymbol, boardRow);

            out.print(SET_BG_COLOR_BLACK);
            out.print(EMPTY);
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if ((boardRow + boardCol) % 2 == 0) {
                    setLightGray(out);
                } else {
                    setDarkGray(out);
                }


                if (printSymbol) {
                    int prefixLength = SQUARE_WIDTH_IN_CHARS / 2;
                    int suffixLength = SQUARE_WIDTH_IN_CHARS - prefixLength - 1;

                    int posRow = (color.equals("white")) ? 8 - boardRow : boardRow + 1;
                    int posCol = (color.equals("white")) ? boardCol + 1 : 8 - boardCol;

                    ChessPosition pos = new ChessPosition(posRow, posCol);

                    out.print(EMPTY.repeat(prefixLength));
                    printPiece(out, getPiece(out, color, pos, board), color);
                    out.print(EMPTY.repeat(suffixLength));
                    out.print(RESET_TEXT_BOLD_FAINT);
                } else {
                    out.print(EMPTY.repeat(SQUARE_WIDTH_IN_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                }

                if (boardCol % 2 == 0) {
                    setBlack(out);
                } else {
                    setWhite(out);
                }
            }
            addColumnHeader(out, columns, printSymbol, boardRow);
            out.print(RESET_BG_COLOR);
            out.println();
        }
    }

//    private static void drawHorizontalLine(PrintStream out) {
//
//        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_WIDTH_IN_CHARS +
//                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_PADDED_CHARS;
//
//        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
//            setRed(out);
//            out.print(EMPTY.repeat(boardSizeInSpaces));
//
//            setBlack(out);
//            out.println();
//        }
//    }

    private static void addColumnHeader(PrintStream out, String[] columns, boolean printSymbol, int boardRow) {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
        out.print(EMPTY);

        if (printSymbol) {
            out.print(columns[boardRow]);
        } else {
            out.print(EMPTY);
        }

        out.print(EMPTY);
    }

    private static String getPiece(PrintStream out, String color, ChessPosition pos, ChessBoard board) {
        ChessPiece piece = board.getPiece(pos);

        if (piece == null) {
            return EMPTY;
        }

        boolean whitePiece = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? true : false;

        out.print((whitePiece) ?
                SET_TEXT_COLOR_GREEN : SET_TEXT_COLOR_BLUE);

        return switch (piece.getPieceType()) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setLightGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setDarkGray(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printPiece(PrintStream out, String player, String color) {
        out.print(SET_TEXT_BOLD);
        out.print(player);
    }

}
