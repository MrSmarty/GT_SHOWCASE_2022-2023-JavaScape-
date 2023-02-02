import javafx.application.Application;
import javafx.stage.Stage;

public class ServerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Server server = new Server(19);
        ServerGUI g = new ServerGUI();

        Thread serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        g.setServer(server);
        g.start(primaryStage);

        serverThread.start();

    }
}
