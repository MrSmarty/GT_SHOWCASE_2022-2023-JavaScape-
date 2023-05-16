import java.util.ArrayList;

import javafx.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
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
import javafx.util.*;

/**
 * The GUI for the Server
 * Pass in the <strong>PrimaryStage</strong> from the ServerApp class
 */
public class ServerGUI {
    public Server server = null;
    Stage pStage;
    BorderPane rootBorderPane;

    Stage popupStage;

    ModalManager modalManager;

    enum Page {
        HOME, USERS, HOUSEHOLDS, DEVICES, SETTINGS
    }

    Page currentPage;

    ObservableList<GridPane> userGrids = FXCollections.observableArrayList();
    ObservableList<GridPane> houseHoldGrids = FXCollections.observableArrayList();
    ObservableList<GridPane> deviceGrids = FXCollections.observableArrayList();

    public ServerGUI() {
    }

    public void start(Stage primaryStage) {
        pStage = primaryStage;
        Platform.setImplicitExit(false);

        primaryStage.setOnCloseRequest(e -> {
            primaryStage.setIconified(true);
            e.consume();
        });

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

        currentPage = Page.HOME;

        primaryStage.show();
    }

    public void setUp(Server s) {
        server = s;
        System.out.println("Set the Server");
        modalManager = new ModalManager(pStage, server);
        System.out.println("Set the ModalManager");

        server.getDataHandler().getUsers().addListener((ListChangeListener.Change<? extends User> c) -> {
            userGrids.clear();
            for (User u : server.getDataHandler().getUsers()) {
                userGrids.add(u.getListGridPane(server, modalManager));
            }
        });
        server.getDataHandler().getHouseHolds().addListener((ListChangeListener.Change<? extends HouseHold> c) -> {
            houseHoldGrids.clear();
            for (HouseHold h : server.getDataHandler().getHouseHolds()) {
                houseHoldGrids.add(h.getListGridPane(server, modalManager));
            }
        });
        server.getDataHandler().getRecievers().addListener((ListChangeListener.Change<? extends Reciever> c) -> {
            deviceGrids.clear();
            for (Reciever r : server.getDataHandler().getRecievers()) {
                deviceGrids.add(r.getListGridPane(server, modalManager));
            }
        });
        for (User u : server.getDataHandler().getUsers()) {
            userGrids.add(u.getListGridPane(server, modalManager));
        }

        for (HouseHold h : server.getDataHandler().getHouseHolds()) {
            houseHoldGrids.add(h.getListGridPane(server, modalManager));
        }

        for (Reciever r : server.getDataHandler().getRecievers()) {
            deviceGrids.add(r.getListGridPane(server, modalManager));
        }

    }

    public void close() {
        pStage.close();
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
        rootBorderPane = new BorderPane();
        rootBorderPane.setTop(createRibbonBar());
        rootBorderPane.setCenter(createHomeBody());

        Scene applicationScene = new Scene(rootBorderPane);
        applicationScene.getStylesheets().add(ServerApp.class.getResource("application.css").toExternalForm());

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

        Label rememberMeLabel = new Label("Remember Me");
        CheckBox rememberMeBox = new CheckBox();
        g.add(rememberMeBox, 1, 4);
        g.add(rememberMeLabel, 0, 4);

        // Messages
        Text message = new Text();
        message.setFill(Color.web("#FE654F"));
        message.setFont(Font.font("Arial", FontWeight.MEDIUM, 15));
        g.add(message, 0, 5, 2, 1);

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
            if (rememberMeBox.isSelected()) {
                Settings.rememberMe = true;
                Settings.rememberedName = userNameField.getText();
                Settings.rememberedPassword = passwordField.getText();
            } else {
                Settings.rememberMe = false;
                Settings.rememberedName = "";
                Settings.rememberedPassword = "";
            }
            login(userNameField.getText(), passwordField.getText(), message);
        });
        g.add(submit, 0, 6, 2, 1);

        if (Settings.rememberMe) {
            if (Settings.autoLogin) {
                login(Settings.rememberedName, Settings.rememberedPassword, message);
            }
            userNameField.setText(Settings.rememberedName);
            passwordField.setText(Settings.rememberedPassword);
            rememberMeBox.setSelected(true);
        } else {
            rememberMeBox.setSelected(false);
        }

        return g;
    }

    private ToolBar createRibbonBar() {
        ToolBar ribbonBar = new ToolBar();
        ribbonBar.setPrefHeight(50);
        ribbonBar.setPrefWidth(100);

        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> {
            rootBorderPane.setCenter(createHomeBody());
            currentPage = Page.HOME;
        });
        Button userButton = new Button("Users");
        userButton.setOnAction(e -> {
            rootBorderPane.setCenter(createUsersBody());
            currentPage = Page.USERS;
        });
        Button houseHoldButton = new Button("Households");
        houseHoldButton.setOnAction(e -> {
            rootBorderPane.setCenter(createHouseholdsBody());
            currentPage = Page.HOUSEHOLDS;
        });
        Button devicesButton = new Button("Devices");
        devicesButton.setOnAction(e -> {
            rootBorderPane.setCenter(createDevicesBody());
            currentPage = Page.DEVICES;
        });
        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> {
            rootBorderPane.setCenter(createSettingsBody());
            currentPage = Page.SETTINGS;
        });

        homeButton.getStyleClass().add("ribbon-button");
        userButton.getStyleClass().add("ribbon-button");
        houseHoldButton.getStyleClass().add("ribbon-button");
        devicesButton.getStyleClass().add("ribbon-button");
        settingsButton.getStyleClass().add("ribbon-button");

        ribbonBar.getItems().addAll(homeButton, userButton, houseHoldButton, devicesButton, settingsButton);

        return ribbonBar;
    }

    private BorderPane createHomeBody() {
        BorderPane body = new BorderPane();
        GridPane optionBar = new GridPane();

        Label threadCount = new Label("Thread Count: " + ServerApp.totalThreadCount());
        Tooltip threadCountTooltip = new Tooltip("Server threads: " + ServerApp.serverThreadCount + "\nClient threads: "
                + ServerApp.clientThreadCount + "\nRecieving threads: " + ServerApp.recieverThreadCount);
        threadCountTooltip.setShowDelay(Duration.millis(0));
        threadCount.setTooltip(threadCountTooltip);
        optionBar.add(threadCount, 0, 0);

        body.setTop(optionBar);
        return body;
    }

    private BorderPane createUsersBody() {
        BorderPane body = new BorderPane();
        GridPane optionBar = new GridPane();

        Button newUser = new Button("New User");
        optionBar.add(newUser, 0, 0);
        newUser.setOnAction(e -> {
            modalManager.createNewUserModal();
        });

        ListView<GridPane> users = new ListView<GridPane>(userGrids);

        body.setCenter(users);

        body.setTop(optionBar);
        return body;
    }

    private BorderPane createHouseholdsBody() {
        BorderPane body = new BorderPane();
        GridPane optionBar = new GridPane();

        Button newHouseHold = new Button("New HouseHold");
        optionBar.add(newHouseHold, 0, 0);
        newHouseHold.setOnAction(e -> {
            modalManager.createNewHouseHoldModal();
        });

        ListView<GridPane> houseHolds = new ListView<GridPane>(houseHoldGrids);

        body.setCenter(houseHolds);

        body.setTop(optionBar);
        return body;
    }

    private BorderPane createDevicesBody() {
        BorderPane body = new BorderPane();
        GridPane optionBar = new GridPane();

        ListView<GridPane> devices = new ListView<GridPane>(deviceGrids);

        body.setCenter(devices);

        body.setTop(optionBar);
        return body;
    }

    private BorderPane createSettingsBody() {
        BorderPane body = new BorderPane();
        GridPane optionsPane = new GridPane();

        Label rememberMeText = new Label("Remember me: ");
        CheckBox rememberMeBox = new CheckBox();
        rememberMeBox.setSelected(Settings.rememberMe);
        rememberMeBox.setOnAction(e -> {
            Settings.rememberMe = rememberMeBox.selectedProperty().getValue();
        });

        Label autologinText = new Label("Autologin: ");
        CheckBox autologinBox = new CheckBox();
        autologinBox.setSelected(Settings.autoLogin);
        autologinBox.setOnAction(e -> {
            Settings.autoLogin = autologinBox.selectedProperty().getValue();
        });

        Label nullCounterLabel = new Label("Null Counter Limit: ");
        Spinner<Integer> nullCounterSpinner = new Spinner<Integer>(1, 1000, Settings.nullCounterLimit, 1);
        nullCounterSpinner.setEditable(true);
        nullCounterSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue != newValue) {
                Settings.nullCounterLimit = Integer.parseInt(newValue);
            }
        });

        Label threadPrinterSecondsLabel = new Label("Thread Printer Seconds: ");
        Spinner<Integer> threadPrinterSecondsSpinner = new Spinner<Integer>(1, 3600, Settings.threadPrinterSeconds, 1);
        threadPrinterSecondsSpinner.setEditable(true);
        threadPrinterSecondsSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue != newValue) {
                Settings.threadPrinterSeconds = Integer.parseInt(newValue);
            }
        });

        Label portLabel = new Label("Port: ");
        Spinner<Integer> portSpinner = new Spinner<Integer>(1, 1000, Settings.port, 1);
        portSpinner.setEditable(true);
        portSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue != newValue) {
                Settings.port = Integer.parseInt(newValue);
            }
        });
        
        optionsPane.add(portLabel, 0, 0);
        optionsPane.add(portSpinner, 1, 0);

        optionsPane.add(rememberMeText, 0, 1);
        optionsPane.add(rememberMeBox, 1, 1);

        optionsPane.add(autologinText, 0, 2);
        optionsPane.add(autologinBox, 1, 2);

        optionsPane.add(nullCounterLabel, 0, 3);
        optionsPane.add(nullCounterSpinner, 1, 3);

        optionsPane.add(threadPrinterSecondsLabel, 0, 4);
        optionsPane.add(threadPrinterSecondsSpinner, 1, 4);


        body.setCenter(optionsPane);

        return body;
    }

    private void login(String username, String password, Text messageField) {
        boolean isAccount = server.authenticate(username, password);
        boolean isAdminAccount = server.authenticateAdmin(username, password);
        if (isAccount) {
            if (isAdminAccount) {
                System.out.println("Admin Account found");
                startApplication(pStage);
            } else {
                System.out.println("Not admin account");
                messageField.textProperty().set("This account does not have Admin privileges");
            }
        } else {
            System.out.println("Account not found");
            messageField.textProperty().set("Account not found");
        }

    }

}