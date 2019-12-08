package audioComparator;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

public class AudioChecker {
    private String data, query;

    public AudioChecker(String data, String query) {
        this.data = data;
        this.query = query;
    }

    public float compare() {
        Wave dataWave = new Wave(this.data);
        Wave queryWave = new Wave(this.query);
        FingerprintSimilarity fs = dataWave.getFingerprintSimilarity(queryWave);
        //getMostSimilarFramePosition
//		System.out.println(fs.getScore());
//		System.out.println(fs.getMostSimilarFramePosition());
        return fs.getScore();
    }
}
