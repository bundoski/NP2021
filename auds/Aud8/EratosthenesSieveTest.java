package auds.Aud8;

/*
1.4. The Sieve of Eratosthenes
Ситото на Ерастотен (The Sieve of Eratosthenes) е древен алгоритам за генерирање прости броеви.
 Да jа земеме следната листа со броеви: 2 3 4 5 6 7 8 9 10

Алгоритмот започнува со првиот прост број во листата, тоа е 2 и потоа ги изминува останатите елементи од листата, со тоа што ги отстранува сите
броеви чии што множител е 2 (во овој случај, 4, 6, 8 и 10), со што остануваат 2 3 5 7. Го повторуваме овој процес со вториот прост број во листата,
тоа е 3 и итерираме низ остатокот од листата и што ги отстрануваме броевите чии што множител е 3 (во овој случај 9), а остануваат 2 3 5 7.
Потоа jа повторуваме постапката со секој следен прост број, но не се отстрануваат елемнти, затоа што нема броеви чии што множители се 5 и 7.
Сите броеви кои остануваат во листата се прости.

Да се имплементира алгоритмот со користење на ArrayList од цели броеви која што е иницијализирана со вредности од 2 до 100.
Имплементацијата може да итерира низ листата со индекс од 0 од size() - 1 за да го земе тековниот прост број, но треба да користи Iterator
за да го скенира остатокот од листата и ги избрише сите елементи чии што множител е тековниот прост број. Отпечатете ги сите прости броеви во листата.
 */

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EratosthenesSieveTest {

    public static boolean isPrime(int number){
        for(int i=0;i<=number/2;i++){
            if(number % i == 0){
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
                                                            //// za da gi pretvorime vo integers.
        ArrayList<Integer> numbers = IntStream.range(2,101).boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        for(int i=0; i<numbers.size();i++){
            if(isPrime(numbers.get(i))){
                for(int j=i+1;j<numbers.size();j++){
                    if(numbers.get(j)%numbers.get(i)==0){
                        numbers.remove(j);
                        --j; // za ko ce go otstrani elemento so treba da se otstrani da se vrati na toj so ce dojdi
                        // na mestoto od otstranetio element, a ne da odi nanapred i da skokni nekoi slucai.
                    }
                }
            }
        }

        System.out.println(numbers);
    }
}
