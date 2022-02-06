package firstMidterm;
import java.awt.im.InputContext;
import java.io.*;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedList;
import java.util.List;

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}

abstract class Item{
    protected int price;
    public Item(int price){
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    abstract public double taxReturn();
}

class ItemA extends Item{
    public ItemA(int price){
        super(price);
    }
    @Override
    public double taxReturn() { return price*0.18*0.15; }
}

class ItemB extends Item{
    public ItemB(int price){
        super(price);
    }
    @Override
    public double taxReturn() {
        return price*0.05*0.15;
    }
}

class ItemC extends Item{
    public ItemC(int price){
        super(price);
    }
    @Override
    public double taxReturn() {
        return 0;
    }

}

class Receipt{
    String id;
    private List<Item> items;

    public Receipt(String line){
        items = new LinkedList<>();
        String[] aux = line.split("\\s+");
        id = aux[0];
        for(int i=1; i<aux.length; i+=2){
            if(aux[i+1].equals("A")) items.add(new ItemA(Integer.parseInt(aux[i])));
            else if(aux[i+1].equals("B")) items.add(new ItemB(Integer.parseInt(aux[i])));
            else items.add(new ItemC(Integer.parseInt(aux[i])));
        }
    }
    public void add(Item item){
        items.add(item);
    }
    public int sum(){ return items.stream().mapToInt(Item::getPrice).sum(); }
    public double totalTaxReturn(){
        return items.stream().mapToDouble(Item::taxReturn).sum();
    }
    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f", id, this.sum(), this.totalTaxReturn());
    }
}

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int sum){
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

class MojDDV {
    private final List<Receipt> bills;

    public MojDDV() {
        bills = new LinkedList<>();
    }

    public void readRecords(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.lines().forEach(l -> {
            Receipt insert = new Receipt(l);
            try {
                if (insert.sum() > 30000) throw new AmountNotAllowedException(insert.sum());
                bills.add(insert);
            } catch (AmountNotAllowedException anae) {
                System.out.println(anae.getMessage());
            }
        });
    }

    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));
        bills.forEach(r -> pw.println(r.toString()));
        pw.flush();
    }

    public void printStatistics(OutputStream outputStream) {
        DoubleSummaryStatistics dss = bills.stream().mapToDouble(Receipt::totalTaxReturn).summaryStatistics();
        PrintWriter pw = new PrintWriter(outputStream);
        pw.println(String.format("min:\t%5.3f\nmax:\t%5.3f\nsum:\t%5.3f\ncount:\t%-5d\navg:\t%5.3f",
                dss.getMin(), dss.getMax(), dss.getSum(), dss.getCount(), dss.getAverage()));
        pw.flush();
    }
}