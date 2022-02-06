package firstMidterm;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class UnsupportedFormatException extends Exception {

    public UnsupportedFormatException(String s){
        super(String.format("%s", s));
    }
}
class InvalidTimeException extends Exception{

    public InvalidTimeException(String s){
        super(String.format("%s", s));
    }
}

class Time24 implements Comparable<Time24>{

    private final Integer hours, minutes;

    public Time24(Integer hours, Integer minutes){
        this.hours = hours;
        this.minutes = minutes;
    }
                                        // gets sent a whole line.
    public static Time24 generateTime(String s) throws UnsupportedFormatException, InvalidTimeException {
        if(!s.contains(".") && !s.contains(":"))
            throw new UnsupportedFormatException(s);
        String [] nums;
        if(s.contains("."))
            nums = s.split("\\.");
        else nums = s.split(":");
        if(nums.length!=2) throw new UnsupportedFormatException(s);
        if(Integer.parseInt(nums[0])>23 || Integer.parseInt(nums[1]) > 59 ){
            throw new InvalidTimeException(s);
        }
        return new Time24(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]));
    }   // generateTime and readTimes done.

    public String getTime(TimeFormat format) {
        if(format == TimeFormat.FORMAT_24)
            return String.format("%2d:%02d", hours, minutes);
        if(hours == 0)
            return String.format("%2d:%02d AM", hours+12, minutes);
        if(hours>=1 && hours<=11)
            return String.format("%2d:%02d AM", hours,minutes);
        if(hours == 12 )
            return String.format("%2d:%02d PM", hours,minutes);
        else
            return String.format("%2d:%02d PM", hours-12, minutes);
    }

    public int compareTo(Time24 o){
        return hours.compareTo(o.hours) == 0? minutes.compareTo(o.minutes) : hours.compareTo(o.hours);
    } // ako hours == o.hours, sporedi gi minutite.
}

class TimeTable {
    private List<Time24> times;

    TimeTable(){
        times = new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws InvalidTimeException, UnsupportedFormatException {
        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNext()) times.add(Time24.generateTime(scanner.next()));
    }

    void writeTimes(OutputStream outputStream, TimeFormat format){

        PrintWriter pw = new PrintWriter(outputStream);

        times.stream().sorted().forEach(t -> pw.println(t.getTime(format)));
        pw.flush();
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}