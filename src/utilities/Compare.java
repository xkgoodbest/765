package utilities;

import java.awt.image.BufferedImage;
import java.util.List;

public class Compare {
    DataLoader loader;
    List<BufferedImage> videoFrames;
    public Compare(DataLoader loader, List<BufferedImage> videoFrames) {
        this.loader = loader;
        this.videoFrames = videoFrames;
    }
}
