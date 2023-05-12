import javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class RaspberryPiPicoW extends Reciever {

    public RaspberryPiPicoW(String id) {
        super(id, "Raspberry Pi Pico W", RaspberryPiPicoW.class.toString());
    }

    public RaspberryPiPicoW(String id, String name) {
        super(id, name, RaspberryPiPicoW.class.getName());
    }

    @Override
    public GridPane getListGridPane(Server s, ModalManager m) {
        GridPane g = new GridPane();

        Label nameLabel = new Label("Name: ");
        Text name = new Text(super.getRecieverName());
        Label idLabel = new Label("ID: ");
        Text id = new Text(super.getID());
        Circle status = new Circle(5);
        if (super.getOnline())
            status.setFill(Color.GREEN);
        else
            status.setFill(Color.RED);

        TitledPane pinEditor = new TitledPane();
        pinEditor.setContent(getPinLayout());
        pinEditor.setText("Pin Editor");

        g.add(status, 0, 0);
        g.add(nameLabel, 1, 0);
        g.add(name, 2, 0);
        g.add(idLabel, 1, 1);
        g.add(id, 2, 1);
        g.add(pinEditor, 1, 2);

        return g;
    }

    private GridPane getPinLayout() {
        GridPane g = new GridPane();

        CheckBox c = new CheckBox();
        CheckBox c2 = new CheckBox();

        c.setOnAction(e -> {
            super.set(0, c.selectedProperty().getValue() ? 1 : 0);
        });
        c2.setOnAction(e -> {
            super.set(1, c.selectedProperty().getValue() ? 1 : 0);
        });

        g.add(c, 0, 0);
        g.add(c2, 0, 1);

        return g;
    }
}
