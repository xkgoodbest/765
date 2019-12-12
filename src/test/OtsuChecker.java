package test;

import com.pragone.jphash.image.radial.RadialHash;
import histComparator.HistResult;
import utilities.DataLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class OtsuChecker {
    DataLoader dl;
    List<HistResult> histResultList;
    List<BufferedImage> videoFrame;

    private static final String[] BASE_NAMES = {"flowers", "interview", "movie", "musicvideo", "sports", "StarCraft", "traffic"};

    public OtsuChecker(DataLoader dl, List<BufferedImage> videoFrame, List<HistResult> histResultList) {
        this.dl = dl;
        this.videoFrame = videoFrame;
        this.histResultList = histResultList;
    }

    public void compare() {
        OtsuRun o = new OtsuRun();
        Map<String, String[]> otsuMap = dl.getOtsu();
        for (int i = 0; i < 7; i++) {
            int firstFrameIdx = histResultList.get(i).getFirstFrameIdx();
            int bestMatchIdx = histResultList.get(i).getBestMatchIdx();

            int startIdx = bestMatchIdx;

            for (int j = firstFrameIdx; j < 150 & startIdx < 600; j++) {
                double otsuScore = getValue(o.xor(o.getBinary(videoFrame.get(j)), otsuMap.get(BASE_NAMES[i])[startIdx++]));
                histResultList.get(i).getSimilarity()[j] += 0.1 * otsuScore;
            }


            startIdx = bestMatchIdx - 1;
            for (int j = firstFrameIdx - 1; j >= 0 & startIdx >= 0; j--) {
                double otsuScore = getValue(o.xor(o.getBinary(videoFrame.get(j)), otsuMap.get(BASE_NAMES[i])[startIdx--]));
                histResultList.get(i).getSimilarity()[j] += 0.1 * otsuScore;
            }
        }
    }

    private double getValue(double val) {
        return val < 0.0 ? 0.0 : Math.min(val, 1.0);
    }

}
