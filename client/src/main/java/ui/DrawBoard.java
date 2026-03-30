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
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    private final String playerColor;
    private final ChessGame game;
    private final ChessBoard board;

    // Padded characters.
    private static final String EMPTY = "   ";

    private static Random rand = new Random();

    public DrawBoard(String playerColor, ChessGame game) {
        this.playerColor = playerColor;
        this.game = game;
        this.board = game.getBoard();
    }

    public void main(String playerColor) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        String[] whiteHeaders = {"A", "B", "C", "D", "E", "F", "G", "H"};
        String[] blackHeaders = {"H", "G", "F", "E", "D", "C", "B", "A"};

        out.print(ERASE_SCREEN);

        String[] headers = (playerColor.equals("WHITE")) ? whiteHeaders : blackHeaders;
        String[] footers = (playerColor.equals("WHITE")) ? blackHeaders : whiteHeaders;

        drawHeaders(out, headers);

        drawChessBoard(out, playerColor, board);

        drawHeaders(out, footers);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private void drawHeaders(PrintStream out, String[] headers) {

        setBlack(out);

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(player);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out, String playerColor, ChessBoard board) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, playerColor, boardRow, board);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw horizontal row separator.
                drawHorizontalLine(out);
                setBlack(out);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, String color, int col, ChessBoard board) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setWhite(out);

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, getPiece(out, color, new ChessPosition(squareRow, col), board));
                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw vertical column separator.
                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                }

                setBlack(out);
            }

            out.println();
        }
    }

    private static void drawHorizontalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_PADDED_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }
    }

    private static String getPiece(PrintStream out, String color, ChessPosition pos, ChessBoard board) {
        ChessPiece piece = board.getPiece(pos);

        return switch (piece.getPieceType()) {
            case KING -> (color.equals("WHITE")) ? WHITE_KING : BLACK_KING;
            case QUEEN -> (color.equals("WHITE")) ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> (color.equals("WHITE")) ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> (color.equals("WHITE")) ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> (color.equals("WHITE")) ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> (color.equals("WHITE")) ? WHITE_PAWN : BLACK_PAWN;
            default -> " ";
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

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }

}
