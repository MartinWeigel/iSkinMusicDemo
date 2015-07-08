# iSkin Music Demo

This application shows an [iSkin](http://www.martinweigel.com/iskin.html) sensor controlling a music player. The GUI has two views: one for calibration and one for visualization. They can be switched by pressing the 'v'-key or the right mouse button. Each interactive area is calibrated by defining a hysteresis threshold. Afterwards, touch events are visualized on the displayed picture of the sensor. The touch input directly controls the music player and plays songs from a predefined playlist (src/MusicPlayer.java).

Sensor data is received over OSC (127.0.0.1:5500). Each OSC message should contain exactly three values: (int) timestamp, (int) channel, (int) value. The sensor data can be collected and forwarded by a micro-controller or oscilloscope. For use with Pico Technology oscilloscopes, check out the [PeakToPeakoscope pipeline](https://github.com/MartinWeigel/PeakToPeakoscope).

This demo application was shown a full-week at CeBIT'15 and four days at ACM CHI'15.

## Libraries
This project is built upon some amazing open-source libraries. For your convenience their unmodified Java archives are included in the library folder. Thanks a lot to the authors and contributors!

 - [Processing Core](https://processing.org) (LGPL)
 - [Control P5](http://www.sojamo.de/libraries/controlP5/) (LGPL)
 - [OSC P5](http://www.sojamo.de/libraries/oscP5/) (LGPL)
 - [Minim](http://code.compartmental.net/tools/minim/) (LGPL)

## Acknowledgements
This software was programmed as part of my work in the [Embodied Interaction Group](http://embodied.mpi-inf.mpg.de) at the [Max-Planck Institute for Informatics](http://www.mpi-inf.mpg.de), the [Cluster of Excellence MMCI](http://www.mmci.uni-saarland.de) and [Saarland University](http://www.uni-saarland.de). It has partially been funded by the Cluster of Excellence on Multimodal Computing and Interaction within the German Federal Excellence Initiative.
