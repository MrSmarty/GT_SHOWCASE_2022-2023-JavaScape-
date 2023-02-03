import javafx.*;
import javafx.application.*;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class ServerGUI {
    public Server server = null;
    Stage pStage;

    public ServerGUI() {
    }

    public void start(Stage primaryStage) {
        pStage = primaryStage;
        primaryStage.setTitle("JavaScape Server");

        primaryStage.setScene(createLogin());

        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public void startApplication(Stage primaryStage) {
        primaryStage.hide();

        primaryStage.setScene(createApplication());

        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);

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
        root.setLeft(createLoginSideBar());
        root.setRight(createLoginPane());

        Scene loginScene = new Scene(root, windowWidth, windowHeight);

        return loginScene;
    }

    private Scene createApplication() {
        BorderPane root = new BorderPane();
        root.setTop(createRibbonBar());
        root.setBottom(createHomeBody());

        Scene applicationScene = new Scene(root);
        return applicationScene;
    }

    private StackPane createLoginSideBar() {
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
        // g.gridLinesVisibleProperty().set(true);
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
        userNameField.setBackground(
                new Background(new BackgroundFill(Color.web("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));

        g.add(userNameLabel, 0, 2);
        g.add(userNameField, 1, 2);

        // Password
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setBackground(
                new Background(new BackgroundFill(Color.web("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));

        g.add(passwordLabel, 0, 3);
        g.add(passwordField, 1, 3);

        // Messages
        Text message = new Text();
        message.setFill(Color.web("#FE654F"));
        message.setFont(Font.font("Arial", FontWeight.MEDIUM, 15));
        g.add(message, 0, 4, 2, 1);

        // Submit
        Button submit = new Button("Submit");
        submit.setBackground(new Background(new BackgroundFill(Color.web("#277D50"), CornerRadii.EMPTY, Insets.EMPTY)));
        submit.textFillProperty().set(Color.WHITE);
        submit.cursorProperty().set(Cursor.HAND);

        submit.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    login(userNameField.getText(), passwordField.getText(), message);
                }
            }
        });
        submit.setOnAction(e -> {
            login(userNameField.getText(), passwordField.getText(), message);
        });
        g.add(submit, 0, 5, 2, 1);

        return g;
    }

    private ToolBar createRibbonBar() {
        ToolBar ribbonBar = new ToolBar();
        ribbonBar.setPrefHeight(50);
        ribbonBar.setPrefWidth(100);

        Button homeButton = new Button("Home");
        Button userButton = new Button("Users");
        Button houseHoldButton = new Button("Households");

        homeButton.setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));
        

        ribbonBar.getItems().addAll(homeButton, userButton, houseHoldButton);

        return ribbonBar;
    }

    private BorderPane createHomeBody() {
        BorderPane body = new BorderPane();


        return body;
    }

    private void login(String username, String password, Text message) {
        boolean isAdminAccount = server.authenticateAdmin(username, password);
        if (isAdminAccount) {
            System.out.println("Admin Account found");
            startApplication(pStage);
        } else {
            System.out.println("Admin Account not found");
            message.textProperty().set("Invalid Username or Password");
        }
    }
}