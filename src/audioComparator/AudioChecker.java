package audioComparator;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;
import histComparator.HistResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioChecker {
    private String query;
    private List<HistResult> histResultList;
    private static final String[] BASE_NAMES = {"flowers", "interview", "movie", "musicvideo", "sports", "StarCraft", "traffic"};


    public AudioChecker(String query, List<HistResult> histResultList) {
        this.query = query;
        this.histResultList = histResultList;
    }

    public void compare() {
        Map<String, Float> map = new HashMap<>();
        Wave queryWave = new Wave(this.query);
        for (int i = 0; i < 7; i++) {
            Wave dataWave = new Wave("db/" + BASE_NAMES[i] + "/" + BASE_NAMES[i] + ".wav");
            FingerprintSimilarity fs = dataWave.getFingerprintSimilarity(queryWave);
            double val = getValue(fs.getScore());

            int firstFrameIdx = histResultList.get(i).getFirstFrameIdx();
            int bestMatchIdx = histResultList.get(i).getBestMatchIdx();

            int startIdx = bestMatchIdx;
            for (int j = firstFrameIdx; j < 150 & startIdx < 600; j++) {
                double base = histResultList.get(i).getSimilarity()[j];
                histResultList.get(i).getSimilarity()[j] = 0.7 * base + 0.1 * val;
            }

            startIdx = bestMatchIdx - 1;
            for (int j = firstFrameIdx - 1; j >= 0 & startIdx >= 0; j--) {
                double base = histResultList.get(i).getSimilarity()[j];
                histResultList.get(i).getSimilarity()[j] = 0.7 * base + 0.1 * val;
            }
        }
        //getMostSimilarFramePosition
//		System.out.println(fs.getScore());
//		System.out.println(fs.getMostSimilarFramePosition());
    }

    private double getValue(double val) {
        return val < 0.0 ? 0.0 : Math.min(val, 1.0f);
    }

}
