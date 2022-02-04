package auds.Aud7;


// import org.w3c.dom.css.Counter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Counter{

    int [] countingArray;
    int digitsCount;

    Counter(){
        countingArray = new int [10];
        digitsCount = 0;
    }

    void countDigit(int digit){
        countingArray[digit]++;
        digitsCount++;
    }

    public String toString(){
        return IntStream.range(1,10)
                .mapToObj(i -> String.format("%d --> %.2f %%", i , countingArray[i]*100.0/digitsCount))
                .collect(Collectors.joining("\n"));
    }
}

class BenfordLaw{

    List<Integer> numbers;
    Counter counter;

   public BenfordLaw(){
       numbers = new ArrayList<>();
       counter = new Counter();
   }

   public void readData(InputStream inputStream){
       BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

       numbers = in.lines()
               .filter(line -> line.length()>0)
               .map(i -> Integer.parseInt(i))
               .collect(Collectors.toList());
   }

   public void conduct(){
       numbers.stream()
               .map(this::getFirstDigit)
               .forEach(firstDigit -> counter.countDigit(firstDigit));
   }

   private int getFirstDigit(int number){
       while(number>=10){
           number/=10;
       }
       return number;
   }

    public String toString(){
        return counter.toString();
    }
}

public class BenfordLawTest {

    public static void main(String[] args) {

        BenfordLaw benfordLaw = new BenfordLaw();

        try{
            benfordLaw.readData(new FileInputStream("C:\\Users\\Sadstro\\Desktop\\FINKI\\NP2021\\src\\librarybooks"));
            benfordLaw.conduct();
            System.out.println(benfordLaw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
