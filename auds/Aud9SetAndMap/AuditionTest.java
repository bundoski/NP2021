package auds.Aud9SetAndMap;


import java.util.*;

class Participant{
    String city;
    String code;
    String name;
    int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString(){
        return String.format("%s %s %d", code, name, age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return code.equals(that.code);
    }
    // go birame hashcode da se izgenerira samo za kod, za da se duplikati site so ce ima ist kod.
    // da nema nesto so ist kod, ama razlicno ime na grad, ili godini, itn.
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}

class Audition{
    Map<String, Set<Participant>> participantsByCity;

    public Audition(){
        participantsByCity = new HashMap<>();
    }


    public void addParticpant(String city, String code, String name, int age) {
        // kreiranje na nov participant
        Participant p = new Participant(city, code, name, age);

        // dodavanje na elementi eden po eden, koga go nema klucho vo mapata, nema ucesnik od grad city.
        if(!participantsByCity.containsKey(city)){
            participantsByCity.put(city, new HashSet<>());
        }
        participantsByCity.get(city).add(p);
    }

    public void listByCity(String city){
        Set<Participant> participantsFromCity = participantsByCity.get(city);
        if(participantsFromCity.isEmpty()){
            System.out.println("NOT FOUND");
        }
        else{
            Comparator<Participant> comparator = Comparator.comparing(Participant::getName).
                    thenComparing(Participant::getAge).thenComparing(Participant::getCode);

            participantsFromCity.stream()
                    .sorted(comparator)
                    .forEach(System.out::println);
        }
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}