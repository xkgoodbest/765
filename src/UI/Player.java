package UI;

import javax.swing.*;

public class Player {
    PlayVideo pv;
    PlayAudio pa;

    public Player(String path, String threadName, JLabel displayImage) {
        pv = new PlayVideo(path, threadName, displayImage);
        pa = new PlayAudio(path, threadName);
//        setAudioPlayer(new AudioPlayer(threadName, audiofilepath, audiofilename));
    }

    public void play() {
        pv.play();
        pa.play();
    }

    public void pause() {
        pv.pause();
        pa.pause();
    }

    public void stop() {
        pv.stop();
        pa.stop();
    }

    public void adjustTimeLine(int point) {
        System.out.println(point);
        pa.setFrame(point);
        pv.currentFrame = point;
    }
}
