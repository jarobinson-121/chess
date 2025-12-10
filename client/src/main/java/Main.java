import ui.LoggedOutClient;
import ui.MainClient;

public class Main {
    public static void main(String[] args) {
        String url = "8080";

        try {
            MainClient mainClient = new MainClient(Integer.parseInt(url));
            mainClient.run();
        } catch (Exception ex) {
            System.out.printf("Failed to start server: %s%n", ex.getMessage());
        }
    }
}