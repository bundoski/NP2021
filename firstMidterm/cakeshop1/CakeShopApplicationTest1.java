package firstMidterm.cakeshop1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Item{

    String name;
    int price;

    public Item(String name, int price){
        this.name = name;
        this.price = price;
    }

    public Item(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

class Order implements Comparable<Order>{
    private int id;
    private List<Item> items;

    public Order(){
        id = -1;
        items = new ArrayList<>();
    }
    public Order(int id, List<Item> items){
        this.id = id;
        this.items = items;
    }

    public static Order createOrder(String line){
        String [] parts = line.split("\\s+");
        int orderId = Integer.parseInt(parts[0]);
        List<Item> items = new ArrayList<>();
        Arrays.stream(parts)
                .skip(1) // skipping 1st because we used it in orderId.
                .forEach(part -> {
                    if(Character.isAlphabetic(part.charAt(0)))
                        items.add(new Item(part));
                    else
                        items.get(items.size()-1).setPrice(Integer.parseInt(part));
                });
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

    public void setItems(List<Item> items) {
        this.items = items;
    }
    @Override
    public String toString(){
        return id + " " + items.size();
    }

    @Override
    public int compareTo(Order o) {
        return Integer.compare(this.items.size(), o.items.size());
    }
}

class CakeShopApplication{

    private List<Order> orders;

    public CakeShopApplication(){
        orders = new ArrayList<>(); // initializing list..
    }

    public int readCakeOrders(InputStream fileInputStream) {
        orders = new BufferedReader(new InputStreamReader(fileInputStream))
                .lines()
                .map(Order::createOrder)
                .collect(Collectors.toList());

        return orders.stream()
                .mapToInt(order -> order.getItems().size()) // gi mapiram elementite od orders vo int, za da mozi da zemime size().
                .sum();
    }

    public void printLongestOrder(PrintStream out) {
        PrintWriter printWriter = new PrintWriter(out);
        Order longestOrder = orders.stream()
                .max(Comparator.naturalOrder())
                .orElseGet(Order::new);
        printWriter.write(longestOrder.toString());
        printWriter.flush();

    }
}

public class CakeShopApplicationTest1 {

    public static void main(String[] args) throws FileNotFoundException {
        CakeShopApplication cakeShopApplication = new CakeShopApplication();

        System.out.println("--- READING FROM INPUT STREAM ---");
        System.out.println(cakeShopApplication.readCakeOrders(new FileInputStream(new File("C:\\Users\\Sadstro\\Desktop\\FINKI\\NP2021\\src\\firstMidterm\\txtfiles\\cakes.txt"))));

        System.out.println("--- PRINTING LARGEST ORDER TO OUTPUT STREAM ---");
        cakeShopApplication.printLongestOrder(System.out);
    }
}