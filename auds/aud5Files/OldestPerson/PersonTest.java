package auds.aud5Files.OldestPerson;


import java.io.*;
import java.nio.Buffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Person implements Comparable<Person>{

    private String name;
    private int age;

    public Person(String name, int age){
        this.name = name;
        this.age = age;
    }

    public Person(String line) {
        // konstruktor za praenje na nov person so map
        String [] parts = line.split(" ");
        this.name = parts[0];
        this.age = Integer.parseInt(parts[1]);
    }

    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

public class PersonTest {

    public static List<Person> ReadData(InputStream inputStream){

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return bufferedReader.lines()
                .map(line -> new Person(line))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        File file = new File("C:\\Users\\Sadstro\\Desktop\\FINKI\\NP2021\\src\\hi.txt");

        try {
            List<Person> people = ReadData(new FileInputStream(file));
            // this way we get the maximum.

            Collections.sort(people);
            System.out.println(people.get(people.size()-1));

            // ako ima takov nekoj element t.e listata ne e prazna, proveruva so
            // ifpresent dali postoi.
            // na ovaj nacin go zemame minimum.

            if(people.stream().min(Comparator.naturalOrder()).isPresent())
            System.out.println(people.stream().min(Comparator.naturalOrder()).get());
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
