package auds.Aud8;

import java.util.Collection;

/*
1.1. Count occurrence
Да се имплементира следниот метод коj го враќа бројот на појавување на стрингот str во колекцијата од колекција од стрингови:

public static int count(Collection<Collection<String>> c, String str)
Да претпоставиме дека Collection c содржи N колекции и дека секоја од овие колекции содржи N објекти. Кое е времето
на извршување на вашиот метод?

Да претпоставиме дека е потребно 2 милисекунди да се изврши за N = 100. Колку ќе биде времето на извршување кога N = 300?
 */

public class CountOccurrrencesTest {
    public static int count1(Collection<Collection<String>> c, String str){
        int counter = 0;
        // . . vaka vleguvame vo sekoja redica vo kolekciite na c.
        for(Collection<String> collection : c ){
            for(String element : collection){    // tuka za da pristapime do elementite na kolekcijata so red br n..
                if(element.equalsIgnoreCase(str)){
                    ++counter;
                }
            }
        }
        return counter;
    }
    // so streams i.e funkcionalno programiranje kakvo bi bilo resenieto :
    public static int count2(Collection<Collection<String>> c, String str){
       return (int) c.stream()
                .flatMap(col -> col.stream()) // dobivame strim od site stringovi sodrzani vo kolekcijata od kolekciite od stringovi
                .filter(string -> string.equalsIgnoreCase(str)) // da ostana tie so se ednakvi so str
                .count();
    }

    public static void main(String[] args) {

    }
}
