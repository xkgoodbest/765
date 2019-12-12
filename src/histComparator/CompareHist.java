package histComparator;

import java.awt.image.BufferedImage;
import java.util.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;
import utilities.utility;

public class CompareHist {

    private static final double BLACK_SIMILARITY = 0.5;
    private static final String[] BASE_NAMES = {"flowers", "interview", "movie", "musicvideo", "sports", "StarCraft", "traffic"};
    private int firstFrameIdx;
    List<BufferedImage> queryVideo;
    Map<String, List<Mat>> histMap;
    int querySize;

    public CompareHist(List<BufferedImage> queryVideo, Map<String, List<Mat>> histMap) {
        this.queryVideo = queryVideo;
        this.histMap = histMap;
        this.firstFrameIdx = getFirstNonBlackFrame();
        this.querySize = queryVideo.size();
        System.out.println(querySize);
    }

    public Mat getHsvMat(BufferedImage bi) {
        Mat srcBase = utility.bufferedImageToMat(bi);

        if (srcBase.empty()) {
            System.err.println("Cannot read the images");
            System.exit(0);
        }
        Mat hsvBase = new Mat(), hsvTest1 = new Mat();
        Imgproc.cvtColor( srcBase, hsvBase, Imgproc.COLOR_BGR2HSV );
        return hsvBase;
    }

    public double compare(Mat query, Mat db) {
        int hBins = 50, sBins = 60;
        int[] histSize = { hBins, sBins };
        // hue varies from 0 to 179, saturation from 0 to 255
        float[] ranges = { 0, 180, 0, 256 };
        // Use the 0-th and 1-st channels
        int[] channels = { 0, 1 };
        Mat histBase = new Mat(), histTest1 = new Mat();

        List<Mat> hsvBaseList = Arrays.asList(query);
        Imgproc.calcHist(hsvBaseList, new MatOfInt(channels), new Mat(), histBase, new MatOfInt(histSize), new MatOfFloat(ranges), false);
        Core.normalize(histBase, histBase, 0, 1, Core.NORM_MINMAX);

        List<Mat> hsvTest1List = Arrays.asList(db);
        Imgproc.calcHist(hsvTest1List, new MatOfInt(channels), new Mat(), histTest1, new MatOfInt(histSize), new MatOfFloat(ranges), false);
        Core.normalize(histTest1, histTest1, 0, 1, Core.NORM_MINMAX);

        double baseTest1 = Imgproc.compareHist( histBase, histTest1, 0 );
        //System.out.println("Method " + " / " + baseTest1);
        return baseTest1;
    }

    public List<HistResult> getHistSimilarity() {
        List<HistResult> res = getBestMatchForFirstFrame();

        for (int i = 0; i < 7; i++) {
            String name = BASE_NAMES[i];
            int startIdx = res.get(i).bestMatchIdx;

            startIdx--;
            for (int j = firstFrameIdx - 1; j >= 0 && startIdx >= 0; j--) {
                res.get(i).similarity[j] = getValue(compare(getHsvMat(queryVideo.get(j)), histMap.get(name).get(startIdx--)));
            }

            startIdx = res.get(i).bestMatchIdx;

            for (int j = firstFrameIdx; j < 150 && startIdx < 600; j++) {
                //System.out.println(j + " " + startIdx);
                res.get(i).similarity[j] = getValue(compare(getHsvMat(queryVideo.get(j)), histMap.get(name).get(startIdx++)));
            }
        }
        Collections.sort(res);
        for (HistResult r : res) {
            System.out.println(r);
        }
        return res;
    }

    private double getValue(double val) {
        return val < 0.0 ? 0.0 : Math.min(val, 1.0);
    }

    public List<HistResult> getBestMatchForFirstFrame() {
        BufferedImage img1 = queryVideo.get(firstFrameIdx);
        Mat hsvBase = getHsvMat(img1);

        System.out.println("start hist");

        List<HistResult> res = new ArrayList<>();

        for (String name : BASE_NAMES) {
            double[] sim = new double[600];
            double max = 0.0;
            int bestMatchIdx = 0;
            int i = 0;
            for (Mat mat : histMap.get(name)) {
                double tmp = getValue(compare(hsvBase, mat));
                if (tmp > max) {
                    bestMatchIdx = i;
                    max = tmp;
                }
                i++;
            }
            res.add(new HistResult(sim, bestMatchIdx, max, name, firstFrameIdx));
        }
        return res;
    }

    private int getFirstNonBlackFrame() {
        int res = 0;
        Mat black = histMap.get("musicvideo").get(0);
        while (res < this.queryVideo.size()) {
            Mat candi = getHsvMat(this.queryVideo.get(res));
            double simToBlack = compare(candi, black);
            System.out.println("similar to black: " + simToBlack);
            if (simToBlack < BLACK_SIMILARITY) {
                System.out.println("similar to black: " + simToBlack);
                break;
            }
            res = res + 4;
        }
        return res > 150 ? 0 : res;
    }
}
