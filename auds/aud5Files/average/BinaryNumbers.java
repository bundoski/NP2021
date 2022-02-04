package auds.aud5Files.average;

import java.io.*;
import java.util.Random;
import java.util.stream.IntStream;

public class BinaryNumbers {

    public static final String FILE_NAME =
            "C:\\Users\\Sadstro\\Desktop\\FINKI\\NP2021\\src\\names.dat";

    private static void generateFile(int n){

        try{

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            Random random = new Random();

            for(int i=0; i<n; i++){
                int nextRandom = random.nextInt(1000);
                objectOutputStream.writeInt(nextRandom);
            }
            objectOutputStream.flush();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static double average(){

        int sum =0;
        int count = 0;
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME));
            try{
                while(true){
                    int number = objectInputStream.readInt();
                    sum+=number;
                    count++;
                }
            } catch(EOFException e){
                System.out.println("End of file reached");
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return sum / count;
    }

    public static void main(String[] args) {

        generateFile(1000);
        System.out.println("Average: " +average());
    }

}
