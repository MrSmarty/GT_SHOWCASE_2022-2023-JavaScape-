import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame window;

    // The elements of the Menu
    private JMenuBar menuBar;
    private JMenu fileMenu;

    private JMenu usersMenu;
    private JMenuItem newUser;
    private JMenuItem deleteUser;
    private JMenu houseHoldsMenu;

    public GUI() {
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

        fileMenu.add(newUser);
        fileMenu.add(deleteUser);

        window.setJMenuBar(menuBar);

        window.setSize(500, 500);
        window.setVisible(true);
    }
}
