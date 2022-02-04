package auds.Aud10DesignPatterns.Decorator;

// Component participant
interface Beverage{
    double getPrice();
    String getDescription();
}
// Concrete components
class Espresso implements Beverage{
    boolean doubleShot;

    public Espresso(boolean singleOrDouble){
        this.doubleShot = singleOrDouble;
    }
    // cenata ce ni zavisi od to dali e single, ili double espresso.
    @Override
    public double getPrice() {
        return doubleShot ? 80 : 40; // if true, 80, else, 40.
    }

    @Override
    public String getDescription() {
        return doubleShot ? "Double espresso" : "Espresso";
    }
}
class BrewCoffee implements Beverage{

    @Override
    public double getPrice() {
        return 50;
    }

    @Override
    public String getDescription() {
        return "Brew coffee";
    }
}
// za da ne prajme EspressoWithMilk, class BrewCoffeeWithMilk, class BrewCoffeeWithMilkAndCream, itn.
// prajme abstraktna klasa BeverageDecorator, vo nea se cuva eden Component t.e Beverage objekt.
// posle izveduvame MilkDecorator, SugarDecorator, CreamDecorator, itn.
abstract class BeverageDecorator implements Beverage{
    Beverage wrappedBeverage;

    public BeverageDecorator(Beverage wrappedBeverage) {
        this.wrappedBeverage = wrappedBeverage;
    }
}
class MilkDecorator extends BeverageDecorator{

    public MilkDecorator(Beverage wrappedBeverage) {
        super(wrappedBeverage);
    }

    @Override
    public double getPrice() {
        return wrappedBeverage.getPrice() + 20; // ja zema cenata od wrapped beverage, i dodava nekoja negova cena.
    }

    @Override
    public String getDescription() {
        String wrappedBeverageDesc = wrappedBeverage.getDescription();
        if(wrappedBeverageDesc.contains(" with")){ // ako sodrzi with, dodaj go to so sodrzi, dodaj zapirka i posle to dodaj milk
            return wrappedBeverageDesc + ", milk";  // ako ne, dodaj samo with milk.
        }else{
            return wrappedBeverageDesc + " with milk";
        }
    }
}
class CreamDecorator extends BeverageDecorator {

    public CreamDecorator(Beverage wrappedBeverage) {
        super(wrappedBeverage);
    }

    @Override
    public double getPrice() {
        return wrappedBeverage.getPrice() + 15;
    }

    @Override
    public String getDescription() {
        String wrappedBeverageDesc = wrappedBeverage.getDescription();
        if(wrappedBeverageDesc.contains(" with")){ // ako sodrzi with, dodaj go to so sodrzi, i milk dodaj.
            return wrappedBeverageDesc + ", cream";
        }else{
            return wrappedBeverageDesc + " with cream";
        }
    }
}
public class CoffeeShopTest {
    public static void main(String[] args) {
        Beverage coffee = new Espresso(true);
        Beverage withMilk = new MilkDecorator(coffee); // wrapping na coffee mu prajme,
        Beverage finalBeverage = new CreamDecorator(withMilk);

        System.out.println(finalBeverage.getPrice());
        System.out.println(finalBeverage.getDescription());
    }
}
