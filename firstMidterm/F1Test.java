/*
Ф1 Трка (30 поени) Problem 1 (7 / 18)
Да се имплементира класа F1Race која ќе чита од влезен тек (стандарден влез, датотека, ...) податоци за времињата од последните 3 круга на неколку пилоти на Ф1 трка. Податоците се во следниот формат:

Driver_name lap1 lap2 lap3, притоа lap е во формат mm:ss:nnn каде mm се минути ss се секунди nnn се милисекунди (илјадити делови од секундата). Пример:

Vetel 1:55:523 1:54:987 1:56:134.

Ваша задача е да ги имплементирате методите:

F1Race() - default конструктор
void readResults(InputStream inputStream) - метод за читање на податоците
void printSorted(OutputStream outputStream) - метод кој ги печати сите пилоти сортирани според нивното најдобро време (најкраткото време од нивните 3 последни круга) 
во формат Driver_name best_lap со 10 места за името на возачот 
(порамнето од лево) и 10 места за времето на најдобриот круг порамнето од десно. Притоа времето е во истиот формат со времињата кои се читаат.
*/
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
