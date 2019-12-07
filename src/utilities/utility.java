package utilities;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class utility {

    public static BufferedImage getBufferedImage(String path) {
        BufferedImage img = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
        try{
            RandomAccessFile raf = new RandomAccessFile(path, "r");
            raf.seek(0);

            byte[] bytes = new byte[(int) 352 * 288 * 3];
            raf.read(bytes);

            int ind = 0;
            for (int y = 0; y < 288; y++) {
                for (int x = 0; x < 352; x++) {
                    byte r = bytes[ind];
                    byte g = bytes[ind + 288 * 352];
                    byte b = bytes[ind + 288 * 352 * 2];

                    int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    img.setRGB(x, y, pix);
                    ind++;
                }
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static List<String> getRGBFiles(String path) {
        List<String> fileStrings = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            fileStrings = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith("rgb")).collect(Collectors.toList());
            Collections.sort(fileStrings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileStrings;
    }
}
