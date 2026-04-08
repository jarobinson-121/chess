package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private final ChessMove move;

    public MakeMoveCommand(String token, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, token, gameID);
        this.move = move;
    }
}
