import javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class RaspberryPiPicoW extends Reciever {

    Pin[] pins = new Pin[28];

    public RaspberryPiPicoW(String id) {
        super(id, "Raspberry Pi Pico W", RaspberryPiPicoW.class.toString());
        for (int i = 0; i < 28; i++) {
            pins[i] = new Pin();
            pins[i].pinNumber = i;
        }
    }

    public RaspberryPiPicoW(String id, String name) {
        super(id, name, RaspberryPiPicoW.class.getName());
        for (int i = 0; i < 28; i++) {
            pins[i] = new Pin();
            pins[i].pinNumber = i;
        }
    }

    public void setAll() {
        String m = "setAll " + pins.length + " ";
        for (int i = 0; i < 28; i++) {
            m += pins[i].type + ":" + (int)pins[i].value + " ";
        }
        super.send(m);
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
        pinEditor.expandedProperty().set(false);

        Button setAll = new Button("Set All");
        setAll.setOnAction(e -> {
            setAll();
        });

        g.add(status, 0, 0);
        g.add(nameLabel, 1, 0);
        g.add(name, 2, 0);
        g.add(idLabel, 1, 1);
        g.add(id, 2, 1);
        g.add(pinEditor, 1, 2, 2, 1);
        g.add(setAll, 1, 3, 2, 1);

        return g;
    }

    private GridPane getPinLayout() {
        GridPane g = new GridPane();

        for (int i = 0; i < 28; i++) {
            if (i < 14)
                g.add(valueTogglePane(i), 0, i);
            else
                g.add(valueTogglePane(i), 1, i-14);
        }

        return g;
    }

    private GridPane valueTogglePane(int pinNum) {
        GridPane g = new GridPane();

        Label pinLabel = new Label(String.format("GPIO % 2d: ", pinNum));
        CheckBox pinBox = new CheckBox();
        Label value = new Label(""+pins[pinNum].value);

        pinBox.setOnAction(e -> {
            super.set(pinNum, pinBox.selectedProperty().getValue() ? 1 : 0);
            pins[pinNum].value = pinBox.selectedProperty().getValue() ? 1 : 0;
        });
        pinBox.selectedProperty().set(pins[pinNum].value == 1);

        g.add(pinLabel, 0, 0);
        g.add(pinBox, 1, 0);
        g.add(value, 2, 0);

        return g;
    }

    public String getPinTypes() {
        String s = "";
        for (int i = 0; i < 28; i++) {
            s += pins[i].type + " ";
        }
        return s.substring(0, s.length() - 1);
    }

    public String getPinValues() {
        String s = "";
        for (int i = 0; i < 28; i++) {
            s += pins[i].value + " ";
        }
        return s.substring(0, s.length() - 1);
    }
}
