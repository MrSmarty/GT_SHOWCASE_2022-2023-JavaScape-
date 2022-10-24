package modules;

import java.time.*;

public class Display {
    int power;
    int ground;
    int[] dataPins;

    int width;

    /**
     * 
     * @param power    index of the pin for power
     * @param ground   index of the pin for ground
     * @param dataPins the indexes (in order) of the four (4) pins to use for data
     */
    public Display(int power, int ground, int[] dataPins) {
        this.power = power;
        this.ground = ground;
        this.dataPins = dataPins;

        width = 16;
    }
}
