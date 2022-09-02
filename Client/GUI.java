import javax.swing.*;

public class GUI {
    private JFrame jFrame;

    public GUI() {
        initializeFrame();

        // Set the frame to visible
        jFrame.setVisible(true);
    }

    private void initializeFrame() {
        jFrame = new JFrame("Client Application");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(600, 600);
    }
}