package lab3;

import java.util.Arrays;
import java.util.Scanner;


interface Item{

    int getPrice();
    String getType();
}

class PizzaItem implements Item {

    String type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(type.equals("Standard") || type.equals("Pepperoni") || type.equals("Vegetarian"))
            this.type = type;
        else
            throw new InvalidPizzaTypeException();
    }

    public int getPrice(){
        if(type.equals("Standard"))
            return 10;
        else if(type.equals("Pepperoni"))
            return 12;
        else if(type.equals("Vegetarian"))
            return 8;
        else
            return 0;
    }

    public String getType(){
        return type;
    }
}

class ExtraItem implements Item{

    String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(type.equals("Ketchup") || type.equals("Coke"))
            this.type = type;
        else
            throw new InvalidExtraTypeException();
    }

    public int getPrice(){
        if(type.equals("Ketchup"))
            return 3;
        else if(type.equals("Coke"))
            return 5;
        else
            return 0;
    }
    public String getType(){
        return type;
    }

}

class Order {

    boolean locked = false;
    private int [] number;
    Item [] items;

    public Order(){
        items = new Item[0];
        number = new int[0];
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if(locked) throw new OrderLockedException();
        else if(count>10) throw new ItemOutOfStockException(item);
        PizzaItem itemPizza;
        ExtraItem itemExtra;
        if(item.getClass().getName().equals("PizzaItem")){
            itemPizza = (PizzaItem) item;
            int index = -1;
            for(int i=0;i<items.length;i++){
                if(items[i].getType().equals(itemPizza.getType())){
                    index = i;
                    break;
                }
            }
            if(index!=-1)
            {
                items[index] = itemPizza;
                number[index] = count;
            }
            else{
                Item [] tmp = new Item[items.length+1];
                items = Arrays.copyOf(items, items.length+1);
                items[items.length-1] = itemPizza;
                number = Arrays.copyOf(number, number.length+1);
                number[number.length-1]=count;
            }
        }else{
            itemExtra = (ExtraItem) item;
            int index = -1;
            for(int i=0;i<items.length;i++){
                if(items[i].getType().equals(item.getType())){
                    index = i;
                }
            }
            if(index!=-1){
                items[index] = itemExtra;
                number[index] = count;
            }
            else{
                Item[] tmp = new Item[items.length+1];
                items = Arrays.copyOf(items, items.length+1);
                items[items.length-1]=itemExtra;
                number = Arrays.copyOf(number, number.length+1);
                number[number.length-1]=count;
            }
        }
    }
    int getPrice(){
        int sum=0;
        for (int i=0;i<items.length;i++) {
            sum+=items[i].getPrice()*number[i];
        }
        return sum;
    }

    void displayOrder(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<items.length;i++){
            sb.append(String.format("%3d.%-15sx%2d%5d$\n",i+1,items[i].getType(),number[i],items[i].getPrice()*number[i]));
        }
        sb.append(String.format("%-22s%5d$\n","Total:",getPrice()));
        System.out.print(sb.toString());
    }

    void removeItem(int idx) throws ArrayIndexOutOfBoundsException, OrderLockedException {
        if (locked)
            throw new OrderLockedException();

        else if(idx<0 || idx>(items.length-1)) {
            throw new ArrayIndexOutOfBoundsException(idx);
        }
        else {
            Item[] tmp = new Item[items.length - 1];
            int[] tmpn = new int[number.length - 1];
            for(int i= 0;i<idx;i++){
                tmp[i]=items[i];
                tmpn[i]=number[i];
            }
            for (int i = idx; i < items.length - 1; i++) {
                if(items[i+1].getClass().getName().equals("PizzaItem")){
                    //System.out.println(items[i+1].getType());
                    PizzaItem x = (PizzaItem) items[i+1];
                    tmp[i] = x;
                }
                else if(items[i+1].getClass().getName().equals("ExtraItem")){
                    //System.out.println(items[i+1].getType());
                    ExtraItem x = (ExtraItem) items[i+1];
                    tmp[i] = x;
                }
                tmpn[i] = number[i + 1];
            }
            items = tmp;
            number = tmpn;
        }
    }

    void lock() throws EmptyOrder{
        if(items.length==0) throw new EmptyOrder();
        else locked = true;
    }
}

class ItemOutOfStockException extends Exception{
    ItemOutOfStockException(Item item){
        super(item.toString());
    }
}
class EmptyOrder extends Exception{
    EmptyOrder(){
        super("EmptyOrder");
    }
}
class OrderLockedException extends Exception{

}

class ArrayIndexOutOfBoundsException extends Exception{

    ArrayIndexOutOfBoundsException(int indx){
        super(Integer.toString(indx));
    }
}

class InvalidExtraTypeException extends Exception{

}

class InvalidPizzaTypeException extends Exception{

}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}