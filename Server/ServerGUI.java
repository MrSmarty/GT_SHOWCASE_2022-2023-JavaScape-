import javax.swing.*;
import javax.swing.border.*;
import javax.xml.crypto.Data;

import java.awt.*;

public class ServerGUI {
    private JFrame window;

    // The elements of the Menu
    private JMenuBar menuBar;
    private JMenu fileMenu;

    private JMenu usersMenu;
    private JMenuItem newUser;
    private JMenuItem deleteUser;
    private JMenu houseHoldsMenu;

    private DataHandler dataHandler;

    public ServerGUI(DataHandler d) {
        dataHandler = d;
        initialize();

    }

    private void initialize() {
        window = new JFrame("Javascape Server Application");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        usersMenu = new JMenu("Users");
        houseHoldsMenu = new JMenu("Households");

        newUser = new JMenuItem("Create new User");
        deleteUser = new JMenuItem("Delete a User");

        menuBar.add(fileMenu);
        menuBar.add(usersMenu);
        menuBar.add(houseHoldsMenu);

        usersMenu.add(newUser);
        usersMenu.add(deleteUser);

        window.setJMenuBar(menuBar);

        window.setSize(500, 500);
        window.setVisible(true);

        newUser.addActionListener(e -> addNewUserPopup());

    }

    private void addNewUserPopup() {
        JFrame popup = new JFrame("Create New User");
        popup.setLayout(new BoxLayout(popup, BoxLayout.X_AXIS));

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel usernamePanel = new JPanel();
        JPanel passwordPanel = new JPanel();

        // usernameField.setBorder(new LineBorder(Color.black, 2));

        usernamePanel.add(new JLabel("Username: "));
        usernamePanel.add(usernameField);
        passwordPanel.add(new JLabel("Password: "));
        // passwordPanel.add(passwordField);

        popup.add(usernamePanel);
        popup.add(passwordPanel);

        popup.pack();
        popup.setVisible(true);
    }
}
