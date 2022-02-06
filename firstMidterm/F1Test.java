package firstMidterm;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Racer{

    private String name;
    private String bestLap;

   public Racer(String s){
        String [] parts = s.split("\\s+");
        String [] laps = {parts[1], parts[2], parts[3]};
        String best = Arrays.stream(laps).min(String::compareTo).orElse("");
        this.name = parts[0];
        this.bestLap = best;
    }
    public String print(int i)
    {
        return String.format("%d. %-10s%10s",i,name,bestLap);
    }
    public int compareTo(Racer r)
    {
        return this.bestLap.compareTo(r.bestLap);
    }
}

class F1Race{

    List<Racer> racers = new ArrayList<>();

    F1Race(){
        this.racers = new ArrayList<>();
    }

    public void readResults(InputStream in) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        racers = bf.lines().map(l -> new Racer(l)).sorted(Racer::compareTo).collect(Collectors.toList());
    }

    public void printSorted(OutputStream out) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        for(int i=0;i<racers.size();i++){
            bw.write(racers.get(i).print(i+1)+"\n");
            bw.flush();
        }
    }
}

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }
}
