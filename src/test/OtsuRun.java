package test;

import utilities.Grayscale;
import utilities.utility;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OtsuRun {

    public String getBinary(BufferedImage img) {
        BufferedImage img1 = Grayscale.toGrayscale(img);
        Raster raster = img1.getData();
        DataBuffer buffer = raster.getDataBuffer();

        DataBufferByte byteBuffer = (DataBufferByte) buffer;
        byte[] srcData = byteBuffer.getData(0);
        byte[] dstData = new byte[srcData.length];
        OtsuThresholder thresholder = new OtsuThresholder();
        thresholder.doThreshold(srcData, dstData);

        char[] dst = new char[dstData.length];
        for(int i = 0; i < dstData.length; i++) {
            if (dstData[i] == 0) {
                dst[i] = '0';
            }
            else {
                dst[i] = '1';
            }
        }
        return new String(dst);
    }

    public static double xor(String s1, String s2) {
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        int cnt = 0;
        for(int i = 0; i < s1.length();i++) {
            cnt += (int)(c1[i]^c2[i]);
        }
        return cnt/(double)s1.length();
    }

    public static void main(String[] args) throws IOException {
        OtsuRun o = new OtsuRun();
        List<String> rgbFiles = utility.getRGBFiles("/Users/xkgoodbest/Programs/576_project/database_videos/traffic");
        String[] res = new String[600];
        int idx = 0;
        for (String s: rgbFiles) {
            BufferedImage img = utility.getBufferedImage(s);
            res[idx] = o.getBinary(img);
            idx++;
        }

        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter("/Users/xkgoodbest/Programs/576_project/765/data/Otsu/traffic.txt"));
        for (int i = 0; i < res.length; i++) {
            // Or:
            outputWriter.write(res[i]);
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();

    }
}
