import java.time.*;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Reciever implements Comparable<Reciever> {
    private int id = -1;
    private String name = "";
    private LocalDateTime lastAccessed;

    public Reciever() {

    }

    public Reciever(int id, String name) {
        this.id = id;
        this.name = name;
        lastAccessed = LocalDateTime.now();
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
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

    public GridPane getListGridPane(Server s, ModalManager m) {
        GridPane g = new GridPane();

        Text name = new Text(this.name);
        Text id = new Text(Integer.toString(this.id));
        Circle status = new Circle(5);
        status.setFill(Color.RED);

        g.add(name, 0, 0);
        g.add(id, 1, 0);
        g.add(status, 0, 1);

        return g;
    }
}
