package auds.Aud10DesignPatterns.Observer;

import java.util.ArrayList;

// This interface handles adding, deleting and updating
// all observers

interface Subject {

    public void register(Observer o);

    public void unregister(Observer o);

    public void notifyObserver();
}

// The Observers update method is called when the Subject changes
interface Observer {

    public void update(double ibmPrice, double aaplPrice, double googPrice);
}

// Uses the Subject interface to update all Observers
class StockGrabber implements Subject {

    // this, can be an array, a map, a set...
    private ArrayList<Observer> observers; // observers that will be updated..
    private double ibmPrice;
    private double aaplPrice;
    private double googPrice;

    public StockGrabber() {
        // Creates an ArrayList to hold all observers
        observers = new ArrayList<>();
    }

    @Override
    public void register(Observer newObserver) {
        // Adds a new observer to the ArrayList
        observers.add(newObserver);
    }

    @Override
    public void unregister(Observer deleteObserver) {
        // Get the index of the observer to delete
        int observerIndex = observers.indexOf(deleteObserver);

        // Print out message (Have to increment index to match)
        System.out.println("Observer " + (observerIndex + 1) + " deleted");
        observers.remove(observerIndex); // removes observer

    }

    @Override
    public void notifyObserver() {
        // Cycle through all observers and notifies them of
        // price changes
        for (Observer observer : observers) {
            observer.update(ibmPrice, aaplPrice, googPrice);
        }
    }

    // notify observers of any changes to come..
    public void setIBMPrice(double newIBMPrice) {

        this.ibmPrice = newIBMPrice;
        notifyObserver();
    }

    public void setAAPLPrice(double newAAPLPrice) {

        this.aaplPrice = newAAPLPrice;
        notifyObserver();
    }

    public void setGOOGPrice(double newGOOGPrice) {

        this.googPrice = newGOOGPrice;
        notifyObserver();
    }
}
// Represents each Observer that is monitoring changes in the subject
class StockObserver implements Observer {

    private double ibmPrice;
    private double aaplPrice;
    private double googPrice;
    // Static used as a counter

    private static int observerIDTracker = 0;
    // Used to track the observers

    private int observerID;
    // Will hold reference to the StockGrabber object

    private Subject stockGrabber;

    public StockObserver(Subject stockGrabber) {
        // Store the reference to the stockGrabber object so
        // I can make calls to its methods
        this.stockGrabber = stockGrabber;
        this.observerID = ++observerIDTracker;
        System.out.println("New Observer " + this.observerID);
        stockGrabber.register(this);
    }

    @Override
    public void update(double ibmPrice, double aaplPrice, double googPrice) {
        this.ibmPrice = ibmPrice;
        this.aaplPrice = aaplPrice;
        this.googPrice = googPrice;

        printThePrices();
    }

    public void printThePrices() {
        System.out.println(observerID + "\nIBM:" + ibmPrice + "\nAPPLE:" + aaplPrice
                + "\nGOOGLE:" + googPrice + "\n");
    }
}

public class GrabStocks {
    public static void main(String[] args) {

        StockGrabber stockGrabber = new StockGrabber();

        StockObserver observer1 = new StockObserver(stockGrabber);
        stockGrabber.setIBMPrice(197.000);
        stockGrabber.setAAPLPrice(1337.00);
        stockGrabber.setGOOGPrice(420.00);

        StockObserver observer2 = new StockObserver(stockGrabber);

        stockGrabber.setIBMPrice(2323);
        stockGrabber.setAAPLPrice(222);
        stockGrabber.setGOOGPrice(22221);
    }
}