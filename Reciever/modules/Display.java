package modules;
import java.time.*;

public class Display {
    String pins;

    public Display(String pins) {
        this.pins = pins;
    }

    public Display(int pins) {
        this.pins = Integer.toBinaryString(pins);
    }
}
