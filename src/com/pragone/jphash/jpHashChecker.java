package com.pragone.jphash;

import com.pragone.jphash.image.radial.RadialHash;
import histComparator.HistResult;
import utilities.DataLoader;
import utilities.utility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class jpHashChecker {

    DataLoader dl;
    List<BufferedImage> queryVideo;
    List<HistResult> histResultList;

    private static final String[] BASE_NAMES = {"flowers", "interview", "movie", "musicvideo", "sports", "StarCraft", "traffic"};

    public jpHashChecker(DataLoader dl, List<BufferedImage> queryVideo, List<HistResult> histResultList) {
        this.dl = dl;
        this.queryVideo = queryVideo;
        this.histResultList = histResultList;
    }

    public void compare() {

        HashMap<String, String[]> phashMap = dl.getPhash();

        for (int i = 0; i < 7; i++) {
            int firstFrameIdx = histResultList.get(i).getFirstFrameIdx();
            int bestMatchIdx = histResultList.get(i).getBestMatchIdx();

            int startIdx = bestMatchIdx;
            for (int j = firstFrameIdx; j < 150 & startIdx < 600; j++) {
                try {
                    double jpHashScore = getValue(jpHash.getSimilarity(jpHash.getImageRadialHash(queryVideo.get(j)),
                            RadialHash.fromString(phashMap.get(BASE_NAMES[i])[startIdx++])));
                    histResultList.get(i).getSimilarity()[j] += 0.1 * jpHashScore;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            startIdx = bestMatchIdx - 1;
            for (int j = firstFrameIdx - 1; j >= 0 & startIdx >= 0; j--) {
                try {
                    double jpHashScore = getValue(jpHash.getSimilarity(jpHash.getImageRadialHash(queryVideo.get(j)),
                            RadialHash.fromString(phashMap.get(BASE_NAMES[i])[startIdx--])));
                    histResultList.get(i).getSimilarity()[j] += 0.1 * jpHashScore;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private double getValue(double val) {
        return val < 0.0 ? 0.0 : Math.min(val, 1.0);
    }
}
