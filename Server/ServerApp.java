import java.net.BindException;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ServerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(ServerApp.class.getResourceAsStream("logo.png")));
        Server server = new Server(19);
        ServerGUI g = new ServerGUI();

        Thread serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (BindException e) {
                System.out.println(
                        "Port already in use.\nIf the server is not currently running, please choose another port in the config file.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        g.setUp(server);
        g.start(primaryStage);

        serverThread.start();

    }
}
