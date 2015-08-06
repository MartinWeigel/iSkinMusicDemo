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

public class MusicSensor extends AbstractSensor {
    public MusicSensor(PApplet applet, ControlP5 cp5) {
        super(applet);

        minRange = 0;
        maxRange = 1023;
        hysteresis = 50;
        electrodes = new ThresholdElectrode[6];
        for (int i = 0; i < electrodes.length; i++)
            electrodes[i] = new ThresholdElectrode(cp5, minRange, maxRange);

        image = applet.loadImage(MusicSensor.class.getClassLoader().getResource("MusicSticker.png").getPath().replaceAll("%20", " "));
        xArea = new int[]{998, 823, 362, HIDE, 876, 748};
        yArea = new int[]{333, 249, 330, HIDE, 462, 501};
        sizeArea = new int[]{120, 180, 120, 0, 110, 110};
    }

    public void activateChannel(int channel) {
        switch (channel) {
            case 0:
                player.next();
                break;
            case 1:
                player.playPause();
                break;
            case 2:
                player.previous();
                break;
            case 4:
                player.volumeUp();
                break;
            case 5:
                player.volumeDown();
                break;
        }
    }
}