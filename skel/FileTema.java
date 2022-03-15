import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FileTema  {

    private String fileName;
    private ConcurrentHashMap<Integer,Integer> hashMap = new ConcurrentHashMap<>();
    private AtomicInteger maxLength = new AtomicInteger(0);
    private AtomicInteger totalWords = new AtomicInteger(0);
    private double rank;

    public FileTema(String fileName) {
        this.fileName = fileName;
    }

    public FileTema(FileTema fileTema){
        this.fileName = fileTema.getFileName();
        this.hashMap = fileTema.getHashMap();
        this.maxLength = fileTema.getMaxLength();
        this.rank = fileTema.rank;
    }

    public AtomicInteger getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(AtomicInteger totalWords) {
        this.totalWords = totalWords;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public AtomicInteger getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(AtomicInteger maxLength) {
        this.maxLength = maxLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ConcurrentHashMap<Integer, Integer> getHashMap() {
        return hashMap;
    }

    public void setHashMap(ConcurrentHashMap<Integer, Integer> hashMap) {
        this.hashMap = hashMap;
    }

}
