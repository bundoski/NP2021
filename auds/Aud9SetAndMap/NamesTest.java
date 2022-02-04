package auds.Aud9SetAndMap;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class NamesTest {

    public static void main(String[] args) {
        try {
            Map<String,Integer> boyNames = getNamesMap("C:\\Users\\Sadstro\\Desktop\\FINKI\\NP2021\\src\\boynames.txt");
            Map<String,Integer> girlNames = getNamesMap("C:\\Users\\Sadstro\\Desktop\\FINKI\\NP2021\\src\\girlnames.txt");

            // find unisex names

            Set<String> uniqueNames = new HashSet<>();
            uniqueNames.addAll(boyNames.keySet());
            uniqueNames.addAll(girlNames.keySet());
            // vaka gi imame site iminja, dali bile vo maski ili zenski, unikatni.
            // gi printa iminata so se pojavuva i kaj zenski i kaj maski.
            /*
            uniqueNames.stream()
                    .filter(name -> boyNames.containsKey(name) && girlNames.containsKey(name))
                    .forEach(name -> System.out.println(name));
            */
            // 1. prosiruvanje na zadaca: Print the frequency of the unisex names ( both male and female)
            Map<String, Integer> unisexNames = uniqueNames.stream()
                    .filter(name -> boyNames.containsKey(name) && girlNames.containsKey(name))
                    .collect(Collectors.toMap(
                            name -> name,
                            name -> boyNames.get(name) + girlNames.get(name)
                    ));

            System.out.println(unisexNames);

            // 2. Print the names and frequency by the frequency in descending order.

            unisexNames.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> System.out.printf("%s: %d\n", entry.getKey(), entry.getValue()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> getNamesMap(String path) throws FileNotFoundException {
        //InputStream is = new FileInputStream(path);
        Map<String, Integer> resultMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

        // first way.
        /* br.lines().forEach(line -> {  // inside lambda funct.
            String [] parts = line.split("\\s+");
            String name = parts[0];
            int freq = Integer.parseInt(parts[1]);
            resultMap.put(name, freq);
        });
        return resultMap;
        */
        return br.lines().collect(Collectors.toMap(
                line -> line.split("\\s+")[0],
                line -> Integer.parseInt(line.split("\\s+")[1])
        ));
    }
}
