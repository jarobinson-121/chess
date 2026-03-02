package exception;

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
}
