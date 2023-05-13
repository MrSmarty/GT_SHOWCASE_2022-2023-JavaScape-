import java.time.*;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

abstract public class Reciever implements Comparable<Reciever> {
    private String id = "-1";
    private String recieverName = "";
    private transient LocalDateTime lastAccessed;
    private boolean isOnline = false;
    private String subType;

    private transient ServerThread currentThread;

    public Reciever() {
        lastAccessed = LocalDateTime.now();
    }

    public Reciever(String id, String name, String subType) {
        this.id = id;
        this.recieverName = name;
        this.subType = subType;
        lastAccessed = LocalDateTime.now();
    }

    public String getSubType() {
        return subType;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void updateLastAccessed() {
        lastAccessed = LocalDateTime.now();
    }

    public int compareTo(Reciever o) {
        return this.lastAccessed.compareTo(o.lastAccessed);
    }

    public void setOnline(boolean b) {
        isOnline = b;
    }

    public boolean getOnline() {
        return isOnline;
    }

    public GridPane getListGridPane(Server s, ModalManager m) {
        GridPane g = new GridPane();

        Label nameLabel = new Label("Name: ");
        Text name = new Text(this.recieverName);
        Label idLabel = new Label("ID: ");
        Text id = new Text(this.id);
        Circle status = new Circle(5);
        if (isOnline)
            status.setFill(Color.GREEN);
        else
            status.setFill(Color.RED);

        g.add(status, 0, 0);
        g.add(nameLabel, 1, 0);
        g.add(name, 2, 0);
        g.add(idLabel, 1, 1);
        g.add(id, 2, 1);

        return g;
    }

    public void set(int pin, int value) {
        currentThread.message = "set " + pin + " " + value;
    }
    
    public int get(int pin) {
        int value = Integer.parseInt(currentThread.awaitResponse("get " + pin));

        return value;
    }

    public void setCurrentThread(ServerThread t) {
        currentThread = t;
    }

    public void clearCurrentThread() {
        currentThread = null;
    }
}
