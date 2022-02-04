package lab6;

import java.util.*;
import java.util.stream.Collectors;

class SuperString{
    LinkedList<String> lista;
    List<String> lastAdded;

    public SuperString(){

        lista = new LinkedList<>();
        lastAdded = new ArrayList<>();
    }

    public void append(String s){
        lista.add(s);
        lastAdded.add(0,s);
    }
    public void insert(String s){
        lista.add(0,s);
        lastAdded.add(0, s);
    }
    public boolean contains(String s){
        return this.toString().contains(s);
    }
    public void reverse(){
        Collections.reverse(lista);
        for(int i=0; i<lista.size();i++){
            lista.set(i, reverseString(lista.get(i)));
            lastAdded.set(i, reverseString(lastAdded.get(i)));
        }
    }
    public void removeLast(int k){
        for(String s : lastAdded.subList(0,k))
            lista.remove(s);
        lastAdded = lastAdded.subList(k , lastAdded.size());
    }

    @Override
    public String toString() {
        return lista.stream().collect(Collectors.joining());
    }

    public String reverseString(String s){
        String reverse ="";
        for(int i=s.length()-1;i>=0;i--){
            reverse+=s.charAt(i);
        }
        return reverse;
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}