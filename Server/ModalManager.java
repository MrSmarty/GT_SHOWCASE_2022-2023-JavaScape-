import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * 
 * This class manages the Modals that pop-up and get user input.
 */
public class ModalManager {

    private Stage parentStage;
    private Server server;
    // The stage for the 
    Stage stage;

    public ModalManager(Stage parentStage, Server server) {
        this.parentStage = parentStage;
        this.server = server;
    }

    public void createNewUserModal() {
        stage = new Stage();
        stage.setTitle("Create New User");
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        GridPane grid = new GridPane();

        Text title = new Text("Create New User");
        grid.add(title, 0, 0, 2, 1);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);

        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);

        Label houseHoldLabel = new Label("Household:");
        ComboBox<Object> houseHoldComboBox = new ComboBox<Object>();
        
        ObservableList<Object> houseHoldNames = FXCollections.observableArrayList();
        server.getDataHandler().getHouseHolds().forEach(e -> {
            houseHoldNames.add(e.getName());
        });

        houseHoldComboBox.setItems(houseHoldNames);

        grid.add(houseHoldLabel, 0, 3);
        grid.add(houseHoldComboBox, 1, 3);

        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            if (!usernameField.getText().equals("")) {
                System.out.println("Test");
            }
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> stage.close());
        grid.add(submit, 0, 4);
        grid.add(cancel, 1, 4);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
