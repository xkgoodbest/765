package test;

import com.pragone.jphash.image.radial.RadialHash;
import com.pragone.jphash.jpHash;
import utilities.utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PHashModel {

    String[] results;
    String origin;
    String dest;
    List<String> files;
    public PHashModel(String origin, String dest){
        this.origin = origin;
        this.dest = dest;
        this.results = new String[600];
        this.files = utility.getRGBFiles(this.origin);
    }

    public void getPHash() throws IOException {
        int i = 0;
        for (String s: files) {
            RadialHash hash = jpHash.getImageRadialHash(s);
            results[i] = hash.toString();
            i++;
        }
    }
    public void write() throws IOException{
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(this.dest));
        for (int i = 0; i < this.results.length; i++) {
            // Or:
            outputWriter.write(this.results[i]);
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }

    public static void main(String[] args) throws IOException {
        PHashModel pm = new PHashModel("/Users/xkgoodbest/Programs/576_project/database_videos/traffic",
                "/Users/xkgoodbest/Programs/576_project/UI/data/phash/traffic.txt");
        pm.getPHash();
        pm.write();
    }
}
