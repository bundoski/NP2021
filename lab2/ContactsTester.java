package lab2;

import java.text.DecimalFormat;
import java.util.*;


class Date {
    private final int day, month, year;

    public Date(int year, int month, int day){
        this.day=day;
        this.month=month;
        this.year=year;
    }

    public static int whoIsNewer(Date d1, Date d2){ //0 ako se isti, 1 ako d1 e ponov, 2 ako e d2 ponov
        if(d1.year == d2.year){
            if(d1.month == d2.month){
                if(d1.day == d2.day)
                    return 0;
                if(d1.day<d2.day)
                    return 1;
                else
                    return 2;
            }
            else if(d1.month<d2.month)
                return 1;
            else
                return 2;
        }
        else if(d1.year<d2.year)
            return 1;
        else
            return 2;
    }
}


abstract class Contact {

    final private String date; //YYYY-MM-DD

    public Contact(String date) {
        this.date = date;
    }

    public boolean isNewerThan(Contact obj){
        return (Date.whoIsNewer(this.convertDate(), obj.convertDate())==2);
        //0 - isti se, 1 - ponov e prviot, 2 - ponov e vtoriot
    }

    public Date convertDate(){
        String [] aux = date.split("-");
        int [] numbers = new int [3];
        for(int i=0; i<3; i++){
            numbers[i]=Integer.parseInt(aux[i]);
        }
        return new Date(numbers[0], numbers[1], numbers[2]);
    }

    abstract public String getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return date.equals(contact.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}


class EmailContact extends Contact{
    private final String email;

    public EmailContact(String date, String email){
        super(date);
        this.email = email;
    }

    public String getEmail(){ return email; }
    public String getType(){ return "Email"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailContact that = (EmailContact) o;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}



class PhoneContact extends Contact {
    private Operator operator;
    private final String number;

    PhoneContact(String date, String number){
        super(date);
        this.number = number;
        int digit = Character.getNumericValue(number.toCharArray()[2]);
        if(digit == 0 || digit == 1 || digit == 2) operator = Operator.TMOBILE;
        else if(digit == 5 || digit == 6) operator = Operator.ONE;
        else if(digit == 7 || digit == 8) operator = Operator.VIP;
    }

    public String getPhone() { return number; }
    public Operator getOperator() { return operator; }
    public String getType() { return "Phone"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneContact that = (PhoneContact) o;
        return operator == that.operator&&number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, number);
    }
}


enum Operator{
    VIP,
    ONE,
    TMOBILE
}

class Student {
    //private Contact [] contacts;
    private final List<Contact> contacts = new ArrayList<>();
    private String firstName, lastName, city;
    private long index;
    private int age;

    public Student(String firstName, String lastName, String city, int age,
                   long index){
        this.firstName=firstName;
        this.lastName=lastName;
        this.city=city;
        this.age=age;
        this.index=index;
    }

    public Student() { }

    public int getNumberOfContacts(){
        return contacts.toArray().length;
    }

    public void addEmailContact(String date, String email){
        contacts.add(new EmailContact(date, email));
    }
    public void addPhoneContact(String date, String phone){
        contacts.add(new PhoneContact(date, phone));
    }

    public Contact [] getEmailContacts(){
        List<Contact> result = new ArrayList<>();
        for(int i=0; i<contacts.toArray().length; i++){
            if(contacts.get(i).getType().equals("Email")){
                result.add(contacts.get(i));
            }
        }
        return result.toArray(new Contact[0]);
    }

    public Contact [] getPhoneContacts(){
        List<Contact> result = new ArrayList<>();
        for(int i=0; i<contacts.toArray().length; i++){
            if(contacts.get(i).getType().equals("Phone")){
                result.add(contacts.get(i));
            }
        }
        return result.toArray(new Contact[0]);
    }

    @Override
    public String toString(){
        return "{" + "\"ime\":\"" + firstName + "\"" +
                ", \"prezime\":\"" + lastName + "\", " +
                "\"vozrast\":" + age + ", " +
                "\"grad\":\"" + city + "\", " +
                "\"indeks\":" + index + ", " +
                "\"telefonskiKontakti\":" + convertPhonesToString(getPhoneContacts()) + ", " +
                "\"emailKontakti\":" + convertEmailsToString(getEmailContacts()) + "}";
    }

    private String convertEmailsToString(Contact [] contacts){
        StringBuilder result = new StringBuilder();
        result.append("[");
        for(int i=0; i<contacts.length; i++){
            result.append("\"").append(((EmailContact) contacts[i]).getEmail()).append("\"");
            if(i!=contacts.length-1) result.append(", ");
        }
        result.append("]");
        return result.toString();
    }

    private String convertPhonesToString(Contact [] contacts){
        StringBuilder result = new StringBuilder();
        result.append("[");
        for(int i=0; i<contacts.length; i++){
            result.append("\"").append(((PhoneContact) contacts[i]).getPhone()).append("\"");
            if(i!=contacts.length-1) result.append(", ");
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return index == student.index&&age == student.age &&
                firstName.equals(student.firstName) &&
                lastName.equals(student.lastName) &&
                city.equals(student.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, city, index, age);
    }

    public String getCity() {
        return city;
    }
    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        Contact aux = contacts.get(0);
        for(int i=1; i<contacts.toArray().length; i++){
            if(contacts.get(i).isNewerThan(aux))
                aux = contacts.get(i);
        }
        return aux;
    }
}
class Faculty {
    String name;
    Student [] students;

    public Faculty(String name, Student [] students){
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String city){
        int counter = 0;
        for (Student student : students)
            if (student.getCity().equals(city)) counter++;
        return counter;
    }

    public Student getStudent(long index){
        for(Student student : students)
            if(student.getIndex() == index) return student;
        return null;
    }

    public double getAverageNumberOfContacts(){
        double sum = 0;
        for(Student student : students){
            sum+=student.getNumberOfContacts();
        }
        return sum/students.length;
    }

    public Student getStudentWithMostContacts(){
        Student aux = new Student();
        int max = 0;
        for (Student student : students) {
            if (student.getNumberOfContacts() >= max) {
                if(student.getNumberOfContacts() > max) {
                    aux = student;
                }else if(student.getNumberOfContacts() == max){
                    if(student.getIndex()>aux.getIndex()) aux = student;
                }
                max = student.getNumberOfContacts();
            }
        }
        return aux;
    }

    private String studentsToString(){
        StringBuilder result = new StringBuilder();
        for(int i=0; i<students.length; i++){
            result.append(students[i].toString());
            if(i!=students.length-1) result.append(", ");
        }
        return result.toString();
    }

    @Override
    public String toString(){
        return "{\"fakultet\":\"" + name + "\", " +
                "\"studenti\":[" + studentsToString() +
                "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return name.equals(faculty.name) &&
                Arrays.equals(students, faculty.students);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(students);
        return result;
    }

}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
