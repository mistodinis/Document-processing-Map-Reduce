import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapping  implements  Runnable{

    private final int id;
    private final int numberOfThreads;
    private  List<Worker> workerList;

    public Mapping(int id, int numberOfThreads, List<Worker> workerList) {
        this.id = id;
        this.numberOfThreads = numberOfThreads;
        this.workerList = workerList;
    }

    @Override
    public void run() {

        int start = (int)(id * (double) workerList.size() / numberOfThreads);
        int end = Math.min((int)((id + 1) * (double) workerList.size() / numberOfThreads), workerList.size());

        HashMap<String , FileInputStream> openedFiles = new HashMap<>();
        HashMap<String  , byte[]> bytesFromEachFile = new HashMap<>();


//        for(int i = start;i < end;i++){
//            String fileName =  workerList.get(i).getFileName();
//            if(!openedFiles.containsKey(fileName)){
//                try {
//                    openedFiles.put(fileName, new FileInputStream(fileName));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        for(Map.Entry<String, FileInputStream> entrySet : openedFiles.entrySet()){
//            Path path = Paths.get(entrySet.getKey());
//            long numberOfBytesInFile;
//            try {
//                numberOfBytesInFile = Files.size(path);
//                byte[] bytes = new byte[(int)numberOfBytesInFile];
//                entrySet.getValue().read(bytes);
//
//                bytesFromEachFile.put(entrySet.getKey() , bytes);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }




        for(int i = start;i < end;i++){

            Worker worker = workerList.get(i);

            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(worker.getFileName(), "r");
                int fileLength = (int) randomAccessFile.length();
                byte[] bytesFromCurrentFile = new byte[fileLength];

                randomAccessFile.read(bytesFromCurrentFile);

                List<Byte> bytesList = new ArrayList<>();



                for(int j = worker.getOffSet() ; j < worker.getOffSet() + worker.getSegmentSize() && j < fileLength; j++){
                    if(j == worker.getOffSet()) {
                        if (checkIfIsLetter(bytesFromCurrentFile[j])) {
                            if (j == 0) {
                                bytesList.add(bytesFromCurrentFile[j]);
                                continue;
                            } else if (!checkIfSplitter(bytesFromCurrentFile[j - 1])) {
                                while (checkIfIsLetter(bytesFromCurrentFile[j++]) && j < worker.getOffSet() + worker.getSegmentSize() && j < fileLength) {
                                }
                            }else{
                                bytesList.add(bytesFromCurrentFile[j]);
                                continue;
                            }
                        }
                    }

                    if(j == worker.getOffSet() + worker.getSegmentSize() - 1){
                        if(!checkIfSplitter(bytesFromCurrentFile[j])){
                            int k = j;
                            while(k < fileLength){
                                if(checkIfIsLetter(bytesFromCurrentFile[k])){
                                    bytesList.add(bytesFromCurrentFile[k++]);
                                }else{
                                    break;
                                }
                            }
                            continue;
                        }else{
                            bytesList.add(bytesFromCurrentFile[j]);
                            continue;
                        }
                    }
                    if(j < fileLength && j < worker.getOffSet() + worker.getSegmentSize()){
                        bytesList.add(bytesFromCurrentFile[j]);
                    }

                }



                Byte[] parseBufferArray = bytesList.toArray(new Byte[0]);
                byte[] arr = new byte[parseBufferArray.length];
                int x = 0;

                for (Byte bytex : parseBufferArray) {
                    arr[x++] = bytex;
                }

                 String workerString = new String(arr);
                String [] words = workerString.split("[\\\\~`;:/?,.><‘\\[\\]{}()'!@#$%^&\\-_+’=*”\"| \t\n\r]");
//                split("[\\\\~`;:/?˜,.><‘\\[\\]{}()'!@#$%ˆ^&\\-_+’=*”\"| \t\n\r]");


                HashMap<Integer , Integer> appearence = new HashMap<>();
                for(String word : words){
                    int length = word.length();
                    if(length == 0){
                        continue;
                    }
                    if(length > worker.getMaxLength()){
                        worker.setMaxLength(length);
                    }
                    if(appearence.containsKey(length)){
                        appearence.put(length, appearence.get(length) + 1);
                    }else{
                        appearence.put(length, 1);
                    }
                }

                worker.setAppearence(appearence);
                worker.setDateString(workerString);


//                System.out.println(workerString);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    public static boolean checkIfSplitter(byte bytex) {

        return  bytex == 9 || bytex == 10 || bytex == 13 || bytex == 32 || bytex == 33 ||
                bytex == 34 || bytex == 35 || bytex == 36 || bytex == 37 || bytex == 38 || bytex == 39
                || bytex == 40 || bytex == 41 || bytex == 42 || bytex == 43 || bytex == 44 || bytex == 45
                || bytex == 46 || bytex == 47 || bytex == 58 || bytex == 59 || bytex == 60 || bytex == 61
                || bytex == 62 || bytex == 63 || bytex == 64 || bytex == 91 || bytex == 92 || bytex == 93
                || bytex == 94 || bytex == 95 || bytex == 96 || bytex == 123 || bytex == 124 || bytex == 125
                || bytex == 126 ;
    }

    public static boolean checkIfIsLetter(byte bytex){
        return (bytex >= 97 && bytex <= 122) || (bytex >= 65 && bytex <= 90);
    }


}
