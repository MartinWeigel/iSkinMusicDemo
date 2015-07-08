//******************************************************************************
// Project: iSkin Music Demo
// Version: 1.0.0 (2015-07-08)
// License: MIT
//
// Developer(s):
// - Martin Weigel <mail@MartinWeigel.com>
//******************************************************************************

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class MusicPlayer {
    private PApplet applet;
    private Minim minim;
    private AudioPlayer player;
    private float volume = 5;
    private int index = 0;

    // These are the played music files. They need to be located inside the resources folder.
    private String[] FILES = {
            "Death Cab For Cutie - Soul Meets Body.mp3",
            "Coldplay - Magic.mp3",
            "Radical Face - Welcome Home.mp3",
            "Mighty Oaks - Just one day.mp3",
            "Peter, Bjorn & John - Young Folks.mp3"
    };


    public MusicPlayer(PApplet applet) {
        this.applet = applet;
        minim = new Minim(applet);
        player = minim.loadFile(currentSongPath(), 1024);
        player.play();

        changeVolume(volume);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public String currentSongName() {
        return FILES[index];
    }

    private String currentSongPath() {
        String path = MusicPlayer.class.getClassLoader().getResource(FILES[index]).getPath().replaceAll("%20", " ");
        System.out.println(path);
        return path;
    }

    public String currentSong() {
        return FILES[index];
    }

    public void changeVolume(float volume) {
        try {
            // TODO: This only works on Mac OS X
            String[] cmd = {"osascript", "-e", "set Volume " + volume};
            Process child = Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
        }
    }

    public void playPause() {
        if (player.isPlaying())
            player.pause();
        else
            player.play();
    }

    public void next() {
        player.pause();
        index++;
        if (index >= FILES.length)
            index = 0;
        player = minim.loadFile(currentSongPath(), 1024);
        player.play();
    }

    public void previous() {
        player.pause();
        index--;
        if (index < 0)
            index = FILES.length - 1;
        player = minim.loadFile(currentSongPath(), 1024);
        player.play();
    }

    public void volumeUp() {
        volume += 2;
        if (volume > 10)
            volume = 10;
        changeVolume(volume);
        playTick();
    }

    public void volumeDown() {
        volume -= 2;
        if (volume < 0)
            volume = 0;
        changeVolume(volume);
        playTick();
    }

    public void playTick() {
        Minim m2 = new Minim(applet);
        AudioPlayer p2 = m2.loadFile(MusicPlayer.class.getClassLoader().getResource("pop.mp3").getPath());
        p2.play();
    }

    public int getVolume() {
        return (int) volume;
    }
}
