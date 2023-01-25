/**
 * This class is used so that printing is a bit easier. It decides if the output goes to the cli or custom GUI
 */
public class Terminal {
    private boolean useGui;
    private GUI gui = null;

    public Terminal(boolean useGui) {
        this.useGui = useGui;
    }

    public Terminal(boolean useGui, GUI gui) {
        this.useGui = useGui;
        this.gui = gui;
    }

    public void println(String message) {
        if (useGui && gui != null) {
            gui.println(message);
        } else {
            System.out.println(message);
        }
    }
}
