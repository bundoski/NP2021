package auds.Aud8;

/*
1.2. Reverse list
Да се напише метод за печатење на колекција во обратен редослед
 со помош на Collections API но без употреба на ListIterator.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReverseListTest {
    public static void main(String[] args) {

        List<Integer> ints = List.of(1,2,3,4,5,6,7,8,9,10);
        reversePrint2(ints);
    }

    private static <T> void reversePrint2(Collection<T> collection){
        List<T> list = new ArrayList<>(collection);
        Collections.reverse(list);
        list.forEach(i -> System.out.println(i));
    }

    private static <T> void reversePrint(Collection<T>collection){
        List<T> list = new ArrayList<>(collection); // initializing list, so we can work with the elements in the collection
        for(int i=list.size()-1; i>=0; i--){
            System.out.println(list.get(i));
        }
    }

}
