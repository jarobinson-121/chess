import ui.loggedOutClient;

public class Main {
    public static void main(String[] args) {
        String url = "8080";
        if (args.length == 1) {
            url = args[0];
        }

        try {
            new loggedOutClient(Integer.valueOf(url));
        } catch (Exception ex) {
            System.out.printf("Failed to start server: %s%n", ex.getMessage());
        }
    }
}