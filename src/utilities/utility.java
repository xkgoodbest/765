package utilities;

import org.apache.commons.io.FileUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class utility {


    public static BufferedImage getBufferedImage(String path) {
        BufferedImage img = new BufferedImage(352, 288, BufferedImage.TYPE_3BYTE_BGR);
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

    public static int hammingDist(String str1, String str2)
    {
        int i = 0, count = 0;
        while (i < str1.length())
        {
            if (str1.charAt(i) != str2.charAt(i))
                count++;
            i++;
        }
        return count;
    }

    public static String[] readTXT(String path) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path));
        List<String> lines = new ArrayList<String>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        String[] arr = lines.toArray(new String[0]);
        return arr;
    }

    public static void save(Mat mat, String name) {
        File file = new File(name);
        int length = (int) (mat.total() * mat.elemSize());
        byte buffer[] = new byte[length];
        mat.get(0, 0, buffer);
        try
        {
            FileUtils.writeByteArrayToFile(file, buffer);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Mat loadMat(String name) {
        File file = new File(name);
        byte[] buffer = new byte[0];
        try
        {
            buffer = FileUtils.readFileToByteArray(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Mat mat = new Mat(288, 352, CvType.CV_8UC3);
        mat.put(0, 0, buffer);
        return mat;
    }

}
