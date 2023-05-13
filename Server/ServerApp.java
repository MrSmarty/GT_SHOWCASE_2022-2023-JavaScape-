import java.net.*;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.lang.*;

public class ServerApp extends Application {
    // Number of threads from Clients
    public static int clientThreadCount = 0;
    // Number of threads used by the server
    public static int serverThreadCount = 0;
    // Number of threads from the recievers (Picos and stuff)
    public static int recieverThreadCount = 0;

    SystemTrayIcon trayIcon = new SystemTrayIcon();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(ServerApp.class.getResourceAsStream("logo.png")));
        Server server = new Server(80);
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

        Thread updateThread = new Thread(() -> {
            serverThreadCount++;
            while (true) {
                try {
                    Thread.sleep(1000);
                    int[] threadCounts = server.getThreadCount();
                    clientThreadCount = threadCounts[0];
                    recieverThreadCount = threadCounts[1];
                } catch (Exception e) {
                    e.printStackTrace();
                }
                server.saveDataHandler();

            }

        });

        server.setGUI(g);
        g.setUp(server);
        trayIcon.popup.getItem(1).addActionListener(e -> {
            System.exit(0);
        });
        trayIcon.popup.getItem(0).addActionListener(e -> {
            primaryStage.show();
        });
        g.start(primaryStage);

        Thread.ofVirtual().start(serverThread);
        Thread.ofVirtual().start(updateThread);

    }

    public static int totalThreadCount() {
        return clientThreadCount + serverThreadCount + recieverThreadCount;
    }
}
