package auds.aud5Files.FunctionalInterfaces;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FuncInterTest {

    public static void main(String[] args) {

        Predicate<Integer> less100 = integer -> integer<100;
        MyComparator myComparator = (a1, a2) -> a1 > a2;
        boolean res = myComparator.compare(2,5);
        System.out.println(res);

        Consumer<String> consumer = System.out::println;

        consumer.accept("asdg");



    }
}


 interface MyComparator {

    public boolean compare(int a1, int a2);

}
