package firstMidterm.cakeShop2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

abstract class Item {
    private String name;
    private int price;

    public Item(String name) {
        this.name = name;
        this.price = 0;
    }

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    abstract Type getType();
}

enum Type {
    CAKE, PIE
}

class Pie extends Item {
    public Pie(String name) {
        super(name);
    }

    @Override
    Type getType() {
        return Type.PIE;
    }

    @Override
    public int getPrice() {
        return super.getPrice() + 50;
    }
}


class Cake extends Item {

    public Cake(String name) {
        super(name);
    }

    @Override
    Type getType() {
        return Type.CAKE;
    }
}

class Order implements Comparable<Order> {
    private int id;
    private List<Item> items;

    public Order() {
        id = -1;
        items = new ArrayList<>();
    }

    public Order(int id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    /**
     * orderId item1Name item1Price item2Name item2Price
     */
    public static Order createOrder(String line, int minItemsPerOrder) throws InvalidOrderException {
        String[] parts = line.split("\\s+");
        int orderId = Integer.parseInt(parts[0]);
        List<Item> items = new ArrayList<>();

        Arrays.stream(parts)
                .skip(1)
                .forEach(part -> {
                    if (Character.isAlphabetic(part.charAt(0))) {
                        if (part.charAt(0) == 'C')  // factory pattern ?
                            items.add(new Cake(part));
                        else
                            items.add(new Pie(part));
                    } else
                        items.get(items.size() - 1).setPrice(Integer.parseInt(part));
                });
        if (items.size() < minItemsPerOrder)
            throw new InvalidOrderException(orderId);
        return new Order(orderId, items);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public int totalItemsSum() {
        return items.stream().mapToInt(Item::getPrice).sum();
    }

    public int totalPies() {
        return (int) items.stream().filter(i -> i.getType().equals(Type.PIE)).count();
    }

    public int totalCakes() {
        return (int) items.stream().filter(i -> i.getType().equals(Type.CAKE)).count();
    }

    /**
     * orderId totalOrderItems totalPies totalCakes totalAmount
     */
    @Override
    public String toString() {
        return id + " " + items.size() + " " + totalPies() + " " + totalCakes() + " " + totalItemsSum();
    }

    @Override
    public int compareTo(Order other) {
        return Integer.compare(this.totalItemsSum(), other.totalItemsSum());
    }
}

class CakeShopApplication {
    private int minItemsPerOrder;
    private List<Order> orders;

    public CakeShopApplication() {
        orders = new ArrayList<>();
    }

    public CakeShopApplication(int minOrderItems) {
        minItemsPerOrder = minOrderItems;
        orders = new ArrayList<>();
    }

    /**
     * This is the place to handle the exception, because this class should
     * decide if the order is added or not. Be careful where you put a try-catch block
     * as it will cost you points on the exam.
     */
    public void readCakeOrders(InputStream in) {
        orders = new BufferedReader(new InputStreamReader(in))
                .lines()
                .map(line -> {
                    try {
                        return Order.createOrder(line, minItemsPerOrder);
                    } catch (InvalidOrderException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void printAllOrders(OutputStream out) {
        PrintWriter printWriter = new PrintWriter(out);
        orders.stream().sorted(Comparator.reverseOrder()).
                forEach(order -> printWriter.println(order.toString()));
        printWriter.flush();
    }
}

class InvalidOrderException extends Exception {

    public InvalidOrderException(int orderId) {
        super(String.format("The order with id %d has less items than the minimum allowed.",
                orderId));
    }
}

public class CakeShopApplicationTest2 {

    public static void main(String[] args) {
        CakeShopApplication cakeShopApplication = new CakeShopApplication(4);

        System.out.println("--- READING FROM INPUT STREAM ---");
        cakeShopApplication.readCakeOrders(System.in);

        System.out.println("--- PRINTING TO OUTPUT STREAM ---");
        cakeShopApplication.printAllOrders(System.out);
    }
}