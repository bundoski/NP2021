package lab3;

import java.io.IOException;
import java.util.*;

class InvalidNameException extends Exception{
    String name;
    public InvalidNameException(String name){
        super("InvalidNameException " + name);
        this.name = name;
    }

}

class InvalidNumberException extends Exception{

    public InvalidNumberException(String number){
        super("InvalidNumberException " + number);
    }
}
class MaximumSizeExceddedException extends Exception{

    public MaximumSizeExceddedException(int size){
        super("Maximum size excedded " + size);
    }
}
class Contact{

    private String name;
    private List<String> phonenumbers;
    private static final int MAX = 5;

    public Contact(String name, String[] numbers) throws InvalidNameException,
            InvalidNumberException, MaximumSizeExceddedException {
        checkName(name);
        phonenumbers = new ArrayList<>(MAX);
        this.name = name;
        for (String n : numbers) {
            if (phonenumbers.size() > MAX) throw new MaximumSizeExceddedException(1);
            checkNumber(n);
            phonenumbers.add(n);
        }
    }

    public Contact(String name, String number) throws InvalidNameException,
            InvalidNumberException, MaximumSizeExceddedException {
        checkName(name);
        checkNumber(number);
        phonenumbers = new ArrayList<>(MAX);
        this.name = name;
        phonenumbers.add(number);
    }

    public Contact(String name) throws InvalidNameException {
        checkName(name);
        this.name = name;
        phonenumbers = new ArrayList<>(MAX);
    }

    public void checkNumber(String number) throws InvalidNumberException{
        if(number.length()>9 || !number.startsWith("07")
            || number.substring(2,3).replaceAll("[349]", "").equals(""))
            throw new InvalidNumberException(number);
    }
    public void checkName(String name) throws InvalidNameException {

        if(name.length()>10
        || name.length()<4 || (0 != name.replaceAll("[a-zA-Z0-9]", "").length()));
        throw new InvalidNameException(name);
    }

    public String getName(){

        return name;
    }

    public String [] getNumbers(){

        String [] result = new String[this.phonenumbers.size()];
        for(int i=0; i<phonenumbers.size();i++)
            result[i] = phonenumbers.get(i);

        Arrays.sort(result);
        return result;
    }

    public void addNumber(String number) throws InvalidNumberException, MaximumSizeExceddedException{

        checkNumber(number);
        phonenumbers.add(number);
        if(phonenumbers.size() > MAX ) throw new MaximumSizeExceddedException(1);
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(name).append("\n").append(phonenumbers.size()).append("\n");
        for (String num : this.getNumbers()) result.append(num).append("\n");

        return result.toString();
    }

    public Contact valueOf(String s) throws InvalidNumberException,
            InvalidNameException,
            MaximumSizeExceddedException {
        String[] lines = s.split("\n");
        return new Contact(lines[0], lines[1].split(" "));
    }
}
class PhoneBook {
    private final Map<String, Contact> contacts;
    private static final int MAX = 250;

    public PhoneBook() {
        contacts = new HashMap<>();
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if (contacts.size() >= MAX) throw new MaximumSizeExceddedException(MAX);
        if (contacts.containsKey(contact.getName())) throw new InvalidNameException(contact.getName());
        contacts.put(contact.getName(), contact);
    }

    public Contact getContactForName(String name) {
        return contacts.get(name);
    }

    public int numberOfContacts() {
        return contacts.size();
    }

    public Contact[] getContacts() {
        return (Contact[]) contacts.keySet().stream().sorted().map(contacts::get).toArray();
    }

    public boolean removeContact(String name) {
        return contacts.remove(name) != null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        contacts.keySet()
                .stream()
                .sorted()
                .forEach(key -> result.append(contacts.get(key).toString()).append("\n"));
        return result.toString();
    }

    public static boolean saveAsTextFile(PhoneBook phoneBook, String path) throws IOException {
        //nemame ucheno za fajlovi
        return true;
    }

    public static PhoneBook loadFromTextFile(String path) {
        //see above
        return new PhoneBook();
    }

    public Contact[] getContactsForNumber(String number_prefix) {
        return (Contact[]) contacts.values()
                .stream()
                .filter(c -> {
                    for (String number : c.getNumbers()) {
                        if (number.startsWith(number_prefix)) return true;
                    }
                    return false; })
                .sorted(Comparator.comparing(c -> c.getName()))
                .toArray();
    }
}
public class PhoneBookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd));//getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}
