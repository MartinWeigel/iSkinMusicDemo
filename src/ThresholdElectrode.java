//******************************************************************************
// Project: iSkin Music Demo
// Version: 1.0.0 (2015-07-08)
// License: MIT
//
// Developer(s):
// - Martin Weigel <mail@MartinWeigel.com>
//******************************************************************************

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Range;
import controlP5.Slider;

enum TouchState {
    None,
    TouchDown,
    Touch,
    TouchUp
}

class ThresholdElectrode {
    private static final long TOUCH_TIME_BLOCK = 500;
    public TouchState state = TouchState.None;
    public float value;
    public Range range;
    public Slider slider;
    private float thresholdTouch;
    private float hysteresis;
    private long lastTouch;

    ThresholdElectrode(ControlP5 cp5, float rangeMin, float rangeMax) {
        slider = cp5.addSlider("S" + this.toString())
                .setRange(rangeMin, rangeMax)
        ;

        range = cp5.addRange("R" + this.toString())
                .setBroadcast(false)
                .plugTo(this)
                .setHandleSize(10)
                .setRange(rangeMin, rangeMax)
                .setBroadcast(true)
        ;
    }

    public void updateUI() {
        slider.setValue(value);
    }

    public void add(float capacitance) {
        value = capacitance;
        if (value < thresholdTouch - hysteresis) {
            if (state == TouchState.None && ((System.currentTimeMillis() - lastTouch) >= TOUCH_TIME_BLOCK))
                state = TouchState.TouchDown;
            else
                state = TouchState.Touch;
            lastTouch = System.currentTimeMillis();
        }
        if (value > thresholdTouch + hysteresis) {
            state = (state == TouchState.Touch) ? TouchState.TouchUp : TouchState.None;
        }
    }

    public void setThreshold(float thresholdTouch, float hysteresis) {
        this.thresholdTouch = thresholdTouch;
        this.hysteresis = hysteresis;
        range.setRangeValues(thresholdTouch - hysteresis, thresholdTouch + hysteresis);
    }

    public void controlEvent(ControlEvent theControlEvent) {
        if (theControlEvent.isFrom("R" + this.toString())) {
            float min = theControlEvent.getController().getArrayValue(0);
            float max = theControlEvent.getController().getArrayValue(1);
            this.thresholdTouch = (float) ((min + max) / 2.0);
            this.hysteresis = (float) ((max - min) / 2.0);
        }
    }
}
