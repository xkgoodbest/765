package histComparator;

public class HistResult implements Comparable<HistResult> {
    double[] similarity;
    int bestMatchIdx;
    double bestMatchSimilarity;
    String name;
    int firstFrameIdx;

    public HistResult(double[] similarity, int bestMatchIdx, double bestMatchSimilarity, String name, int firstFrameIdx) {
        this.similarity = similarity;
        this.bestMatchIdx = bestMatchIdx;
        this.bestMatchSimilarity = bestMatchSimilarity;
        this.name = name;
        this.firstFrameIdx = firstFrameIdx;
    }

    @Override
    public int compareTo(HistResult other) {
        return other.bestMatchSimilarity < this.bestMatchSimilarity ? -1 : 1;
    }

    public String toString() {
        return "Best similar is " + bestMatchSimilarity + " at " + bestMatchIdx + " of " + name;
    }

    public double[] getSimilarity() {
        return similarity;
    }

    public int getBestMatchIdx() {
        return bestMatchIdx;
    }

    public double getBestMatchSimilarity() {
        return bestMatchSimilarity;
    }

    public String getName() {
        return name;
    }

    public int getFirstFrameIdx() {
        return firstFrameIdx;
    }
}
