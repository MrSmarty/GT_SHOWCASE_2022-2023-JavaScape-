import javafx.*;
import javafx.application.*;
import javafx.stage.*;

public class ServerGUI extends Application {
    private Server server;
    public ServerGUI(Server s) {
        server = s;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Welcome");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
