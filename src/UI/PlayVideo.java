package UI;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import utilities.utility;

public class PlayVideo implements Runnable{
    String threadName;
    JLabel displayImage;
    Thread thisThread;
    int currentFrame;
    List<BufferedImage> videoFrames = new ArrayList<>();
    public PlayVideo(String path, String threadName, JLabel displayImage) {
        this.threadName = threadName;
        getVideo(path);
        this.displayImage = displayImage;
        this.currentFrame = 0;
    }

    @Override
    public void run() {
        Thread th = Thread.currentThread();
        while(th == this.thisThread) {
            playFrame();
            try {
                TimeUnit.MILLISECONDS.sleep(33 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.currentFrame++;
            if (currentFrame >= this.videoFrames.size()) {
                stop();
            }
        }
    }

    public void play() {
        if(this.thisThread == null) {
            this.thisThread = new Thread(this, threadName);
            this.thisThread.start();
        }
    }

    private void playFrame() {
        this.displayImage.setIcon(new ImageIcon(this.videoFrames.get(this.currentFrame)));
    }

    public void stop() {
        this.thisThread = null;
        this.currentFrame = 0;
        playFrame();
    }

    public void pause() {
        this.thisThread = null;
    }

    private void getVideo(String path) {
        List<String> fileStrings = utility.getRGBFiles(path);
        for(String s: fileStrings) {
            BufferedImage img = utility.getBufferedImage(s);
            this.videoFrames.add(img);
        }
    }
}
