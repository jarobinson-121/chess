package requests;

public record CreateGameRequest(String gameName) {

    @Override
    public String toString() {
        return "CreateGameRequest{" +
                "name='" + gameName + '\'' +
                '}';
    }
}
