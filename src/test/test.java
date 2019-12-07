package test;

import com.pragone.jphash.image.radial.RadialHash;
import com.pragone.jphash.jpHash;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        Mat source, template;
//        //将文件读入为OpenCV的Mat格式
//        source = Highgui.imread("/Users/xkgoodbest/Pictures/My Sony A6400/OUTPUT/DSC00138.jpg");
//        template = Highgui.imread("/Users/xkgoodbest/Downloads/WX20191206-155407@2x.png");
//        //创建于原图相同的大小，储存匹配度
//        Mat result = Mat.zeros(source.rows() - template.rows() + 1, source.cols() - template.cols() + 1, CvType.CV_32FC1);
//        //调用模板匹配方法
//        Imgproc.matchTemplate(source, template, result, Imgproc.TM_SQDIFF_NORMED);
//        //规格化
//        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
//        //获得最可能点，MinMaxLocResult是其数据格式，包括了最大、最小点的位置x、y
//        Core.MinMaxLocResult mlr = Core.minMaxLoc(result);
//        Point matchLoc = mlr.minLoc;
//        //在原图上的对应模板可能位置画一个绿色矩形
//        Core.rectangle(source, matchLoc, new Point(matchLoc.x + template.width(), matchLoc.y + template.height()), new Scalar(0, 255, 0));
//        //将结果输出到对应位置
//        Highgui.imwrite("/Users/xkgoodbest/Downloads/匹配结果.jpeg", source);




        RadialHash hash1 = jpHash.getImageRadialHash("/Users/xkgoodbest/Programs/576_project/database_videos/traffic/traffic171.rgb");
        System.out.println("Hash1: " + hash1);
        RadialHash hash2 = jpHash.getImageRadialHash("/Users/xkgoodbest/Programs/576_project/database_videos/traffic/traffic008.rgb");
        System.out.println("Hash2: " + hash2);

        System.out.println("Similarity: " + jpHash.getSimilarity(hash1, hash2));
    }
}