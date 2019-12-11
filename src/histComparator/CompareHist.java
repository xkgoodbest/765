package histComparator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Range;
import org.opencv.imgproc.Imgproc;
import utilities.Compare;
import utilities.DataLoader;
import utilities.utility;

import javax.imageio.ImageIO;

public class CompareHist {
    public Mat getHsvMat(String queryPath) {
        BufferedImage img1 = utility.getBufferedImage(queryPath);
        Mat srcBase = utility.bufferedImageToMat(img1);

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

    public static void main(String[] args) {
        // Load the native OpenCV library

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        DataLoader dl = new DataLoader();
        dl.loadHistogram("data");

        String[] baseNames = {"flowers", "interview", "movie", "musicvideo", "sports", "StarCraft", "traffic"};

        String queryPath = "/Users/kai/Documents/CSCI576/query_videos_2/SeenInexactMatch/HQ2/HQ2_003.rgb";

        Map<String, List<Mat>> histogram = dl.getHistogram();

        long start = System.currentTimeMillis();
        CompareHist histComparator = new CompareHist();

        Mat hsvBase = histComparator.getHsvMat(queryPath);

        double[] max = new double[7];
        int[] idx = new int[7];
        int j = 0;
        for (String name : baseNames) {
            int i = 0;
            for (Mat mat : histogram.get(name)) {
                double tmp = histComparator.compare(hsvBase, mat);;
                if (tmp > max[j]) {
                    idx[j] = i;
                    max[j] = tmp;
                }
                i++;
            }
            j++;
        }

        for (int i = 0; i < 7; i++) {
            System.out.println(max[i] + " " + idx[i] + " " + baseNames[i]);
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
