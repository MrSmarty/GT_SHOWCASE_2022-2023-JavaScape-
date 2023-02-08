import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class ModalManager {

    private Stage parentStage;
    private Server server;

    public ModalManager(Stage parentStage, Server server) {
        this.parentStage = parentStage;
        this.server = server;
    }

    public void createNewUserModal() {
        Stage stage = new Stage();
        stage.setTitle("Create New User");
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        GridPane grid = new GridPane();
        grid.setBackgroundFill(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        Text title = new Text("Create New User");
        grid.add(title, 0, 0, 2, 1);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);

        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> stage.close());
        grid.add(submit, 0, 4);
        grid.add(cancel, 1, 4);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
