package lab7;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class Anagrams {
    public static void main(String[] args) {

        findAll(System.in);
    }
    public static void findAll(InputStream inputStream){
        TreeMap<String, List<String>> map = new TreeMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.lines().forEach(l -> {
            String sorted = sortString(l), key = null;
            for(String k : map.keySet())
                if(sorted.equals(sortString(k))) key = k;
            if(key == null){
                key = l;
            }
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(l);
        });

        map.keySet().stream().sorted().forEach(k -> {
            if (map.get(k).size() > 4) {
                map.get(k).forEach(v -> System.out.print(v + ((map.get(k).indexOf(v)==map.get(k).size()-1)?"":" ")));
                System.out.println();
            }
        });
    }

    public static String sortString(String string){
        char [] tmp = string.toCharArray();
        Arrays.sort(tmp);
        return new String(tmp);
    }
}
