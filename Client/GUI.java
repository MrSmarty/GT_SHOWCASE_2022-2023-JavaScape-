import javax.swing.*;
// import java.awt.*;
import java.awt.event.*;

public class GUI {
    private JFrame jFrame;
    private JPanel p;
    private JTextArea textArea;
    private JTextField textField;

    Action submit;

    public GUI() {
        initializeFrame();

        // Set the frame to visible
        jFrame.setVisible(true);
        initializeTerminal();
    }

    private void initializeFrame() {
        jFrame = new JFrame("Client Application");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(600, 600);
    }

    private void initializeTerminal() {
        p = new JPanel();

        submit = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                textArea.append("$ " + text + "\n");
                textField.setText(null);
            }
        };

        textArea = new JTextArea(10, 25);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textField = new JTextField(25);

        textArea.setEditable(false);

        textField.addActionListener(submit);

        p.add(scrollPane);
        p.add(textField);

        jFrame.add(p);
        jFrame.pack();

    }

    public void println(String text) {
        textArea.append(text + "\n");
    }

    public void print(String text) {
        textArea.append(text);
    }

    private void loginScreen() {

    }

}