//******************************************************************************
// Project: iSkin Music Demo
// Version: 1.0.0 (2015-07-08)
// License: MIT
//
// Developer(s):
// - Martin Weigel <mail@MartinWeigel.com>
//******************************************************************************

import controlP5.ControlP5;
import processing.core.PApplet;

public class ClickWheelSensor extends AbstractSensor {
    public ClickWheelSensor(PApplet applet, ControlP5 cp5) {
        super(applet);

        minRange = 15000;
        maxRange = 65000;
        hysteresis = 1000;
        electrodes = new ThresholdElectrode[4];
        for (int i = 0; i < electrodes.length; i++)
            electrodes[i] = new ThresholdElectrode(cp5, minRange, maxRange);

        image = applet.loadImage(ClickWheelSensor.class.getClassLoader().getResource("ClickWheel.png").getPath());
        xArea = new int[]{1010, 1321, 1010, 693};
        yArea = new int[]{798, 455, 107, 455};
        sizeArea = new int[]{140, 140, 140, 140};
    }

    public void activateChannel(int channel) {
        switch (channel) {
            case 0:
                player.playPause();
                break;
            case 1:
                player.next();
                break;
            case 2:
                player.playPause();
                break;
            case 3:
                player.previous();
                break;
        }
    }
}
