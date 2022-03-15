import java.util.*;
import java.util.concurrent.BrokenBarrierException;

public class Reducing implements Runnable {

    private final int id;
    private final int numberOfThreads;
    private List<FileTema> fileTemaList;
    private List<Worker> workerList;
    private double[] rang;

    public Reducing(int id, int numberOfThreads, List<FileTema> fileTemaList, List<Worker> workerList, double[] rang) {
        this.id = id;
        this.numberOfThreads = numberOfThreads;
        this.fileTemaList = fileTemaList;

        this.workerList = workerList;
        this.rang = rang;
    }

    private synchronized static int fibonacci(int rank) {

        int[] result = new int[rank + 2];


        if (rank < 0 || rank == 0) {
            System.out.println("Rank invalid! ");
            return 0;
        }
        result[0] = 0;
        result[1] = 1;

        if (rank == 1) {
            return 1;
        }

        for (int i = 2; i <= rank + 1; i++) {
            result[i] = result[i - 1] + result[i - 2];

        }
        return result[rank];
    }

    @Override
    public void run() {

        int start = (int) (id * (double) workerList.size() / numberOfThreads);
        int end = Math.min((int) ((id + 1) * (double) workerList.size() / numberOfThreads), workerList.size());

        HashMap<String, FileTema> hashOfFiles = new HashMap<>();

        for (FileTema fileTema : fileTemaList) {
            hashOfFiles.put(fileTema.getFileName(), fileTema);
        }


        for (int i = start; i < end; i++) {
            Worker worker = workerList.get(i);

            FileTema fileTema = hashOfFiles.get(worker.getFileName());


            int maximumLength = fileTema.getMaxLength().intValue();

            for (Map.Entry<Integer, Integer> entrySet : worker.getAppearence().entrySet()) {
                fileTema.getTotalWords().addAndGet(entrySet.getValue());

                if (maximumLength < entrySet.getKey()) {
                    maximumLength = entrySet.getKey();
                    if (fileTema.getMaxLength().get() < maximumLength) {
                        fileTema.getMaxLength().set(maximumLength);
                    }
                }
                if (fileTema.getHashMap().containsKey(entrySet.getKey())) {
                    fileTema.getHashMap().put(entrySet.getKey(), fileTema.getHashMap().get(entrySet.getKey()) + entrySet.getValue());
                } else {
                    fileTema.getHashMap().put(entrySet.getKey(), entrySet.getValue());
                }
            }
        }

        try {
            Tema2.barrier.await();

            start = (int) (id * (double) fileTemaList.size() / numberOfThreads);
            end = Math.min((int) ((id + 1) * (double) fileTemaList.size() / numberOfThreads), workerList.size());

            for (int i = start; i < end; i++) {
                FileTema fileTema = fileTemaList.get(i);


                for (Map.Entry<Integer, Integer> mapEntry : fileTema.getHashMap().entrySet()) {
                    int numberOfAppearence = mapEntry.getValue();


                    rang[i] += fibonacci(mapEntry.getKey() + 1) * numberOfAppearence;
                }
                rang[i] /= fileTema.getTotalWords().get();
                fileTemaList.get(i).setRank(rang[i]);
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
