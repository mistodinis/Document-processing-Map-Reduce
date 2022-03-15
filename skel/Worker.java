import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker {

    private String fileName;
    private int offSet;
    private int segmentSize;
    private HashMap<Integer,Integer> appearence = new HashMap<>();
    private int maxLength = 0;
    String dateString;

    public Worker(String fileName, int offSet, int segmentSize) {
        this.fileName = fileName;
        this.offSet = offSet;
        this.segmentSize = segmentSize;
    }

    public HashMap<Integer, Integer> getAppearence() {
        return appearence;
    }

    public void setAppearence(HashMap<Integer, Integer> appearence) {
        this.appearence = appearence;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }
}
