import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Tema2 {

    public static CyclicBarrier barrier;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }
        int numberOfThreads = Integer.parseInt(args[0]);
        Tema2.barrier = new CyclicBarrier(numberOfThreads);
        Thread[] threads = new Thread[numberOfThreads];

        File rootFile = new File(args[1]);

        try {
            Scanner scanner = new Scanner(rootFile);
            int segmentSize = Integer.parseInt(scanner.nextLine());
            int numberOfFiles = Integer.parseInt(scanner.nextLine());
            List<String> files = new ArrayList<>();
            List<String> workers = new ArrayList<>();
            List<Worker> workerList =Collections.synchronizedList(new ArrayList<>());
            List<FileTema> fileTemaList = Collections.synchronizedList(new ArrayList<>());

            FileWriter myWriter = new FileWriter(args[2]);

            while (scanner.hasNextLine()) {
                files.add(scanner.nextLine());
            }

            for (String file : files) {

                Path path = Paths.get(file);
                long numberOfBytesInFile = Files.size(path);
                fileTemaList.add(new FileTema(file));

//                System.out.println(numberOfBytesInFile);

                for(long offSet = 0;offSet < numberOfBytesInFile;offSet+= segmentSize){
                    if(offSet + segmentSize >= numberOfBytesInFile){
                        workerList.add(new Worker(file,(int)offSet, (int) (numberOfBytesInFile-offSet)));
                        break;
                    }

                    workerList.add(new Worker(file, (int) offSet,segmentSize));
                }



            }

            scanner.close();

            for (int i = 0; i < numberOfThreads; i++) {
                threads[i] = new Thread(new Mapping(i, numberOfThreads,  workerList));
            }

            for (int i = 0; i < numberOfThreads; i++) {
                threads[i].start();
            }

            for (int i = 0; i < numberOfThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            double[] rang = new double[fileTemaList.size()];

            for (int j = 0; j < numberOfThreads; j++) {
                threads[j] = new Thread(new Reducing(j, numberOfThreads,  fileTemaList,  workerList,rang));
            }

            for (int j = 0; j < numberOfThreads; j++) {
                threads[j].start();
            }

            for (int j = 0; j < numberOfThreads; j++) {
                try {
                    threads[j].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            fileTemaList.sort(new FileComparator());

            for (FileTema fileTema : fileTemaList) {
                String [] findName = fileTema.getFileName().split("/");



//                System.out.println(fileTema.getFileName() + " ->" + String.format("%.2f", fileTema.getRank()) + " , " + fileTema.getMaxLength() + " , " + fileTema.getHashMap().get(fileTema.getMaxLength().get()));
                myWriter.write(findName[findName.length - 1] + "," + String.format("%.2f", fileTema.getRank()) + ',' + fileTema.getMaxLength() + ',' + fileTema.getHashMap().get(fileTema.getMaxLength().get()) + "\n");
            }
            myWriter.close();

//            System.out.println("checkOut");
        } catch (FileNotFoundException e) {
            System.out.println("Scanner initiation failed!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
