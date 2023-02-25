import javax.lang.model.util.ElementScanner14;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.PieChart.Data;
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

    /**
     * Creates the popup for creating a new user
     */
    public void createNewUserModal() {
        stage = new Stage();
        stage.setTitle("Create New User");
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        GridPane grid = new GridPane();
        grid.setBackground(new Background(new BackgroundFill(Color.web("#345894"), new CornerRadii(0), Insets.EMPTY)));

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
        ComboBox<String> houseHoldComboBox = new ComboBox<String>();

        ObservableList<String> houseHoldNames = FXCollections.observableArrayList();
        server.getDataHandler().getHouseHolds().forEach(e -> {
            houseHoldNames.add(e.getName());
        });

        houseHoldComboBox.setItems(houseHoldNames);

        grid.add(houseHoldLabel, 0, 3);
        grid.add(houseHoldComboBox, 1, 3);

        CheckBox adminBox = new CheckBox("Admin");
        grid.add(adminBox, 0, 4);

        Text errorText = new Text();
        grid.add(errorText, 0, 5);

        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            if (!usernameField.getText().equals("") && !usernameField.getText().equals("")) {
                if (passwordField.getText().length() >= 4)
                    if (houseHoldComboBox.getValue() != null && !houseHoldComboBox.getValue().equals("")) {
                        server.getDataHandler()
                                .addUser(new User(usernameField.getText(), passwordField.getText(),
                                        server.getDataHandler().findHouseHold(houseHoldComboBox.getValue()),
                                        adminBox.selectedProperty().get()));
                        server.saveDataHandler();
                        stage.close();
                    } else {
                        server.getDataHandler()
                                .addUser(new User(usernameField.getText(), passwordField.getText(),
                                        adminBox.selectedProperty().get()));
                        server.saveDataHandler();
                        stage.close();
                    }
                else {
                    errorText.setText("Minimum password length is 4 characters");
                }
            } else {
                errorText.setText("Please input a Username and Password");
            }
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> stage.close());
        grid.add(submit, 0, 6);
        grid.add(cancel, 1, 6);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates the popup for creating a new user
     */
    public void editUserModal(User u) {
        stage = new Stage();
        stage.setTitle("Edit User");
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        GridPane grid = new GridPane();
        grid.setBackground(new Background(new BackgroundFill(Color.web("#345894"), new CornerRadii(0), Insets.EMPTY)));

        Text title = new Text("Editing: " + u.getName());
        grid.add(title, 0, 0, 2, 1);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setText(u.getName());
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);

        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        passwordField.setText(u.getPassword());
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);

        Label houseHoldLabel = new Label("Household:");
        ComboBox<String> houseHoldComboBox = new ComboBox<String>();

        ObservableList<String> houseHoldNames = FXCollections.observableArrayList();
        server.getDataHandler().getHouseHolds().forEach(e -> {
            houseHoldNames.add(e.getName());
        });

        houseHoldComboBox.setItems(houseHoldNames);
        if (u.getHouseHoldID() != -1)
            houseHoldComboBox.setValue(server.getDataHandler().findHouseHold(u.getHouseHoldID()).getName());

        grid.add(houseHoldLabel, 0, 3);
        grid.add(houseHoldComboBox, 1, 3);

        CheckBox adminBox = new CheckBox("Admin");
        grid.add(adminBox, 0, 4);
        adminBox.selectedProperty().set(u.isAdmin());

        Text errorText = new Text();
        grid.add(errorText, 0, 5);

        Button submit = new Button("Save");
        submit.setOnAction(e -> {
            if (usernameField.getText() != "") {
                u.setName(usernameField.getText());
            }
            if (passwordField.getText() != "") {
                u.setPassword(passwordField.getText());
            }
            if (houseHoldComboBox.getValue() != null && !houseHoldComboBox.getValue().equals("") && u.getHouseHoldID() != server.getDataHandler().findHouseHold(houseHoldComboBox.getValue()).getID()) {
                u.setHouseHoldID(server.getDataHandler().findHouseHold(houseHoldComboBox.getValue()).getID());
                server.getDataHandler().findHouseHold(u.getHouseHoldID()).deleteUser(u);
                server.getDataHandler().findHouseHold(houseHoldComboBox.getValue()).addUser(u);
            }
            u.setAdmin(adminBox.selectedProperty().get());

            server.saveDataHandler();
            stage.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> stage.close());
        grid.add(submit, 0, 6);
        grid.add(cancel, 1, 6);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
