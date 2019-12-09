package utilities;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class DataLoader {
    final static String[] baseNames = {"flowers", "interview", "movie", "musicvideo", "sports", "starcraft", "traffic"};
    HashMap<String, String[]> otsu;
    HashMap<String, String[]> phash;

    public DataLoader() {
        this.otsu = new HashMap<>();
        this.phash = new HashMap<>();
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
}
