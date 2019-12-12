package utilities;

import audioComparator.AudioChecker;
import com.pragone.jphash.jpHashChecker;
import histComparator.CompareHist;
import histComparator.HistResult;
import test.OtsuChecker;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compare {

    List<BufferedImage> videoFrames;
    DataLoader dl;
    String audioPath;

    public Compare(DataLoader dl, List<BufferedImage> videoFrames, String audioPath) {
        this.dl = dl;
        this.videoFrames = videoFrames;
        this.audioPath = audioPath;
        System.out.println(audioPath);
    }

    public List<HistResult> run() {
        CompareHist compareHist = new CompareHist(videoFrames, dl.getHistogram());
        List<HistResult> res = compareHist.getHistSimilarity();

        AudioChecker audioChecker = new AudioChecker(audioPath, res);
        audioChecker.compare();

        jpHashChecker jpChecker = new jpHashChecker(dl, videoFrames, res);
        jpChecker.compare();

        OtsuChecker otsuChecker = new OtsuChecker(dl, videoFrames, res);
        otsuChecker.compare();

        return res;
    }
}
