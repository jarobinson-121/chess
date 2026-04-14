package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private final ChessMove move;
    private final String from;
    private final String to;

    public MakeMoveCommand(String token, Integer gameID, ChessMove move, String from, String to) {
        super(CommandType.MAKE_MOVE, token, gameID);
        this.move = move;
        this.from = from;
        this.to = to;
    }

    public ChessMove getMove() {
        return move;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
