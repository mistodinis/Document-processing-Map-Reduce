import java.util.Comparator;

public class FileComparator implements Comparator<FileTema> {

    @Override
    public int compare(FileTema o1, FileTema o2) {
        if(o1.getRank() > o2.getRank()){
            return -1;
        }else if(o1.getRank() < o2.getRank()){
            return 1;
        }else{

            if(o1.getMaxLength().get() > o2.getMaxLength().get()){
                return -1;
            }else if(o1.getMaxLength().get() < o2.getMaxLength().get()){
                return 1;
            }else{
                return o1.getFileName().compareTo(o2.getFileName());
            }
        }
    }
}
