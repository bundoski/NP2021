package auds.Aud9SetAndMap;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapIntro {

    public static void main(String[] args) {

        Map<String,String> treeMap = new TreeMap<>();

        treeMap.put("FINKI", "FINKI");
        treeMap.put("FinKi", "Finki");
        treeMap.put("NP", "Napredno programiranje");
        treeMap.put("F", "Fakultet");
        treeMap.put("I", "Informaticki");

        System.out.println(treeMap);


        Map<String,String> hashMap = new HashMap<>();
    }
}
