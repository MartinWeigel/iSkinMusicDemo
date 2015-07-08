//******************************************************************************
// Project: iSkin Music Demo
// Version: 1.0.0 (2015-07-08)
// License: MIT
//
// Developer(s):
// - Martin Weigel <mail@MartinWeigel.com>
//******************************************************************************

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public abstract class AbstractSensor {
    public MusicPlayer player;
    protected PApplet applet;
    protected int HIDE = -10000;
    protected PImage image;
    protected int[] xArea;
    protected int[] yArea;
    protected int[] sizeArea;
    // Sensing variables
    protected ThresholdElectrode[] electrodes;
    protected int minRange;
    protected int maxRange;
    protected int hysteresis;

    public AbstractSensor(PApplet applet) {
        this.applet = applet;
        player = new MusicPlayer(applet);
    }

    public void draw(int maxWidth, int maxHeight) {
        applet.fill(0, 0);
        applet.stroke(255, 0, 0);
        applet.strokeWeight(10);

        applet.imageMode(PConstants.CENTER);
        float aspectRatio = (float) image.width / image.height;
        float usedWidth = (maxWidth < maxHeight * aspectRatio) ? maxWidth : maxHeight * aspectRatio;
        float margin = usedWidth / 10;
        applet.image(image, maxWidth / 2, maxHeight / 2, usedWidth - margin, (usedWidth - margin) / aspectRatio);

        for (int i = 0; i < electrodes.length; i++) {
            if (electrodes[i].state != TouchState.None)
                applet.ellipse(xArea[i], yArea[i], sizeArea[i], sizeArea[i]);
        }
    }

    public abstract void activateChannel(int channel);


    /***
     * GETTER FOR VARIABLES
     */
    public int getElectrodesCount() {
        return electrodes.length;
    }

    public ThresholdElectrode getElectrodes(int index) {
        return electrodes[index];
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public int getHysteresis() {
        return hysteresis;
    }

    public void setThresholds() {
        for (ThresholdElectrode electrode : electrodes)
            electrode.setThreshold((minRange + maxRange - minRange) / 2, hysteresis);
    }
}
