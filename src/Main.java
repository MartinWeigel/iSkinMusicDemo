//******************************************************************************
// Project: iSkin Music Demo
// Version: 1.0.0 (2015-07-08)
// License: MIT
//
// Developer(s):
// - Martin Weigel <mail@MartinWeigel.com>
//******************************************************************************

import controlP5.ControlP5;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

enum UIMode {Calibration, Visualization}

public class Main extends PApplet {
    // Display constants
    private final int MARGIN = 40;
    private final int UI_HEIGHT = 60;
    private final int UI_SPACING = 10;
    private int footerColor = color(114, 160, 193);

    // Display variables
    private ControlP5 cp5;
    private UIMode uiMode = UIMode.Calibration;
    private AbstractSensor sensor;

    // Nicer music bar filtering
    private FFT fft;
    private int FFT_COUNT_TO = 5;
    private int fftCounter = 0;
    private double[] frequenceBars = new double[100];
    private float filterFreq = 1.0f / FFT_COUNT_TO;

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "Main"});
    }

    private void toggleUI() {
        uiMode = (uiMode == UIMode.Calibration) ? UIMode.Visualization : UIMode.Calibration;

        for (int i = 0; i < sensor.getElectrodesCount(); i++) {
            if (uiMode == UIMode.Calibration) {
                sensor.getElectrodes(i).range.show();
                sensor.getElectrodes(i).slider.show();
            } else {
                sensor.getElectrodes(i).range.hide();
                sensor.getElectrodes(i).slider.hide();
            }
        }
    }

    public void keyPressed() {
        if (key == 'v')
            toggleUI();
    }

    public void mousePressed() {
        if (mouseButton == LEFT)
            println("Click Coordinates:\t" + mouseX + "\t" + mouseY);
        else
            toggleUI();
    }

    public void setup() {
        cp5 = new ControlP5(this);
        sensor = new ClickWheelSensor(this, cp5);
        size(displayWidth, displayHeight);
        smooth();

        for (int i = 0; i < sensor.getElectrodesCount(); i++) {
            int margin = UI_HEIGHT + UI_SPACING;
            sensor.getElectrodes(i).slider
                    .setPosition(margin + MARGIN, MARGIN + margin * i)
                    .setSize(width - margin - MARGIN * 2, UI_HEIGHT / 2);
            sensor.getElectrodes(i).range
                    .setPosition(margin + MARGIN, MARGIN + margin * i + UI_HEIGHT / 2)
                    .setSize(width - margin - MARGIN * 2, UI_HEIGHT / 2);
        }
        sensor.setThresholds();

        fft = new FFT(sensor.player.getPlayer().bufferSize(), sensor.player.getPlayer().sampleRate());
        new OscP5(this, 5500);
    }

    public void draw() {
        background(255);
        int TEXT_ZONE = 125;

        // Draw Box
        stroke(footerColor);
        fill(footerColor);
        rect(0, height - TEXT_ZONE, width, TEXT_ZONE);

        if (uiMode == UIMode.Calibration) {
            cursor(ARROW);
            fill(0);
            strokeWeight(1);
            stroke(0);

            textFont(createFont("Helvetica", 24, true), 24);

            for (int i = 0; i < sensor.getElectrodesCount(); i++) {
                if (sensor.getElectrodes(i).state == TouchState.None)
                    fill(0, 0, 0);
                else
                    fill(255, 0, 0);
                ellipse(MARGIN + UI_HEIGHT / 2, MARGIN + UI_HEIGHT / 2 + i * (UI_HEIGHT + UI_SPACING), UI_HEIGHT, UI_HEIGHT);

                sensor.getElectrodes(i).updateUI();
            }
        } else {
            noCursor();
            // Draw Background Vibes
            AudioPlayer player = sensor.player.getPlayer();

            if (fftCounter == FFT_COUNT_TO) {
                fftCounter = 0;
                fft.forward(player.mix);
            } else
                fftCounter++;

            float barWidth = (float) (1.0 * width) / frequenceBars.length;
            for (int i = 0; i < frequenceBars.length; i++) {
                frequenceBars[i] = frequenceBars[i] * (1 - filterFreq) + fft.getBand(i) * filterFreq;
                rect(i * barWidth, height - TEXT_ZONE, barWidth, -(float) frequenceBars[i] * 15);
            }

            // Draw Sensor in Foreground
            sensor.draw(width - 2 * MARGIN, height - TEXT_ZONE);
        }

        // Draw Playing Symbol
        stroke(0);
        fill(0);
        int marginSymbol = 30;
        if (sensor.player.isPlaying()) {
            triangle(marginSymbol, height - TEXT_ZONE + marginSymbol, marginSymbol + 50, height - TEXT_ZONE / 2, marginSymbol, height - marginSymbol);
        } else {
            rect(marginSymbol, height - TEXT_ZONE + marginSymbol, TEXT_ZONE - 2 * marginSymbol, TEXT_ZONE - 2 * marginSymbol);
        }

        // Draw Headings
        fill(0);
        textSize(18);
        text("Current Song", TEXT_ZONE, height - 85);
        text("Volume", width * 3 / 4, height - 85);

        textSize(32);
        text(sensor.player.currentSongName(), TEXT_ZONE, height - 40);

        // Draw Volume Bars
        int barWidth = 30;
        stroke(0);
        fill(0);
        for (int i = 0; i < sensor.player.getVolume() / 2; i++) {
            rect(width * 3 / 4 + 100 + (i * (barWidth * 3 / 2)), height - 93, barWidth, barWidth * i / 5);
        }
    }

    public void oscEvent(OscMessage msg) {
        int channel = msg.get(1).intValue();
        if (channel < sensor.getElectrodesCount()) {
            sensor.getElectrodes(channel).add(msg.get(2).floatValue());
            if (sensor.getElectrodes(channel).state == TouchState.TouchDown)
                sensor.activateChannel(channel);
        }
    }
}