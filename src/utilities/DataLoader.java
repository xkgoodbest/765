package utilities;

import org.opencv.core.Mat;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataLoader {
    final static String[] baseNames = {"flowers", "interview", "movie", "musicvideo", "sports", "StarCraft", "traffic"};
    private HashMap<String, String[]> otsu;
    private HashMap<String, String[]> phash;
    private HashMap<String, List<Mat>> histogram;

    public DataLoader() {
        this.otsu = new HashMap<>();
        this.phash = new HashMap<>();
        this.histogram = new HashMap<>();
    }

    public void loadOTSU(String otsuPath){
        try{
            for(String name: baseNames) {
                String filePath = otsuPath + "/Otsu/" + name + ".txt";
                this.otsu.put(name, utility.readTXT(filePath));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void loadPHash(String phashPath){
        try {
            for(String name: baseNames) {
                String filePath = phashPath + "/phash/" + name + ".txt";
                this.phash.put(name, utility.readTXT(filePath));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadHistogram(String histPath) {
        for(String name: baseNames) {

            List<Mat> list = new ArrayList<>();
            for (int i = 1; i <= 600; i++) {
                //System.out.println("deserialising - " + name + i);
                String histFilePath = String.format("%s/%s%03d.histogram",
                        histPath + "/histogram/" + name, name, i);
                //System.out.println(histFilePath);
                Mat mat = utility.loadMat(histFilePath);
                list.add(mat);
            }
            histogram.put(name, list);
        }

        System.out.println("hist done");
    }

    public HashMap<String, String[]> getOtsu() {
        return otsu;
    }

    public HashMap<String, String[]> getPhash() {
        return phash;
    }

    public HashMap<String, List<Mat>> getHistogram() {
        return histogram;
    }
}
