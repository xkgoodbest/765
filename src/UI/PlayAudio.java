package UI;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayAudio {
    String threadName;
    int currentFrame;
    private FileInputStream inputStream;
    private AudioInputStream audioInputStream = null;
    private Clip clip = null;
    private long microsec = 0;
    int state = 0;
    String path;

    public PlayAudio(String path, String threadName) {
        this.threadName = threadName;
        this.currentFrame = 0;
        this.path = path;
        initAudio();
    }

    private void initAudio() {
        List<String> fileStrings = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(this.path))) {
            fileStrings = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith("wav")).collect(Collectors.toList());
            Collections.sort(fileStrings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream = new FileInputStream(fileStrings.get(0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            InputStream bufferedIn = new BufferedInputStream(inputStream);
            audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AudioFormat audioFormat = audioInputStream.getFormat();
        try {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
    public void play() {
        if (state == 1)
            return;
        if (state == 0)
            initAudio();
        if(this.microsec != 0)
        {
            clip.setMicrosecondPosition(this.microsec);
            this.microsec = 0;
        }
        clip.start();
        state = 1;
    }

    public void stop() {
        if (state == 0)
            return;
        clip.stop();
        clip.drain();
        clip.close();
        this.microsec = 0;
        state = 0;
    }

    public void pause() {
        if (state == 1) {
            clip.stop();
            state = 2;
        }
    }
    public void setFrame(int idx) {
        clip.setMicrosecondPosition(idx * 1000000/30);
    }
}
