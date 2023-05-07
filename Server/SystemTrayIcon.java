import java.awt.*;

public class SystemTrayIcon {

    public PopupMenu popup;

    public SystemTrayIcon() {
        if (SystemTray.isSupported()) {
            System.out.println("SystemTray is supported");
        } else {
            System.out.println("SystemTray is not supported");
            return;
        }

        popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(
                Toolkit.getDefaultToolkit().getImage(ServerApp.class.getResource("logo.png")));

        final SystemTray tray = SystemTray.getSystemTray();

        // Create the menu components
        MenuItem open = new MenuItem("Open Window");
        MenuItem close = new MenuItem("Close");
        

        popup.add(open);
        popup.add(close);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }

    }
}
