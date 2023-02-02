import javafx.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class ServerGUI {
    public Server server = null;

    public ServerGUI() {
    }

    
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaScape Server");

        primaryStage.setScene(createLogin());

        primaryStage.setResizable(false);

        primaryStage.show();
    }


    public void setServer(Server s) {
        server = s;
        System.out.println("Set the Server");
    }


    private Scene createLogin() {
        int windowHeight = 400;
        int windowWidth = 600;

        BorderPane root = new BorderPane();
        root.setLeft(createSideBar());
        root.setRight(createLoginPane());

        Scene loginScene = new Scene(root, windowWidth, windowHeight);

        return loginScene;
    }

    private StackPane createSideBar() {
        StackPane sideBar = new StackPane();
        sideBar.setPrefWidth(300);
        sideBar.setPrefHeight(400);
        sideBar.setStyle("-fx-background-color: #FFFFFF;");

        // The logo and the class to view it
        Image logo = new Image(ServerApp.class.getResourceAsStream("logo.png"), 250, 250, false, false);
        ImageView logoView = new ImageView(logo);
        

        sideBar.getChildren().add(logoView);
        return sideBar;
    }

    private GridPane createLoginPane() {
        GridPane g = new GridPane();
        g.setPrefHeight(400);
        g.setPrefWidth(300);
        g.setStyle("-fx-background-color: #E1DCC9;");
        //g.gridLinesVisibleProperty().set(true);
        g.setPadding(new Insets(20, 20, 20, 20));
        g.setVgap(20);
        g.setHgap(10);
        g.setAlignment(Pos.CENTER);

        // Title
        Text title = new Text("JavaScape Server");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        title.setFill(Color.web("#345894"));
        g.add(title, 0, 0, 2, 2);
        

        // Username
        Label userNameLabel = new Label("Username:");
        TextField userNameField = new TextField();

        g.add(userNameLabel, 0, 2);
        g.add(userNameField, 1, 2);

        // Password
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        g.add(passwordLabel, 0, 3);
        g.add(passwordField, 1, 3);

        // Submit
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            boolean isAccount = server.authenticate(userNameField.getText(), passwordField.getText());
            if (isAccount) {
                System.out.println("Account found");
            } else {
                System.out.println("Account not found");
            }
        });
        g.add(submit, 0, 4, 2, 1);
        

        return g;
    }
}