package exception;

import java.util.Map;

import com.google.gson.Gson;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        BadRequest,
        AlreadyTakenError,
        Unauthorized
    }

    private final Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }

    public int toHttpStatusCode() {
        return switch (code) {
            case Code.ServerError -> 500;
            case Code.BadRequest -> 400;
            case Code.Unauthorized -> 401;
            case Code.AlreadyTakenError -> 403;
        };
    }
}
