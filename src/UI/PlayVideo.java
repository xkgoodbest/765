package UI;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<String> fileStrings = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            fileStrings = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith("rgb")).collect(Collectors.toList());
            Collections.sort(fileStrings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long len = 352 * 288 * 3;
        for(String s: fileStrings) {
            BufferedImage img = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
            try{
                RandomAccessFile raf = new RandomAccessFile(s, "r");
                raf.seek(0);

                byte[] bytes = new byte[(int) len];
                raf.read(bytes);

                int ind = 0;
                for (int y = 0; y < 288; y++) {
                    for (int x = 0; x < 352; x++) {
                        byte r = bytes[ind];
                        byte g = bytes[ind + 288 * 352];
                        byte b = bytes[ind + 288 * 352 * 2];

                        int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                        img.setRGB(x, y, pix);
                        ind++;
                    }
                }
                this.videoFrames.add(img);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
