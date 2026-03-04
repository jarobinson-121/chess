package requests;

public record CreateGameRequest(String name) {

    @Override
    public String toString() {
        return "CreateGameRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
