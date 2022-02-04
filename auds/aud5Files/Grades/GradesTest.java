package auds.aud5Files.Grades;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Student implements Comparable<Student> {

    private String lastName;
    private String firstName;
    private int exam1;
    private int exam2;
    private int exam3;
    private char grade;

    public Student(String lastName, String firstName, int exam1,
                   int exam2, int exam3) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.exam1 = exam1;
        this.exam2 = exam2;
        this.exam3 = exam3;
        setGrade();  // go povikuvame za da setira i grade.
    }

    public char getGrade() {
        return grade;
    }

    public double totalPoints(){
        return 0.25*exam1 + 0.3*exam2 + 0.45*exam3;
    }

    public void setGrade() {

        double points = totalPoints();

        if(points >= 90){
            this.grade = 'A';
        }
        else if(points>=80){
            this.grade = 'B';
        }
        else if(points>=70){
            this.grade = 'C';
        }
        else if(points>=60){
            this.grade = 'D';
        }
        else if(points>=50){
            this.grade = 'E';
        }
        else this.grade= 'F';

    }

    public static Student createStudent(String line){

        String [] parts = line.split(":");
        return new Student(parts[0],parts[1], Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + grade;
    }

    public String printFullInformation(){

        return String.format("%s %s %d %d %d %.2f %c", lastName, firstName, exam1,exam2,exam3,totalPoints(), grade);
    }

    @Override
    public int compareTo(Student o) {
        return Character.compare(this.grade, o.grade);
    }
}

class Course{

    private List<Student> students;

    public Course(){
        students = new ArrayList<>();
    }

    public void readData(InputStream inputStream){

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.students = bufferedReader.lines()
                .map(line -> Student.createStudent(line))
                .collect(Collectors.toList());
    }

    public void printSortedData(OutputStream outputStream){

        PrintWriter printWriter = new PrintWriter(outputStream);
        this.students.stream().sorted().forEach(s -> System.out.println(s));
        printWriter.flush();
    }

    public void printDetailedData(OutputStream outputStream){

        PrintWriter printWriter = new PrintWriter(outputStream);
        this.students.forEach(i -> printWriter.println(i.printFullInformation()));
        printWriter.flush();
    }

    public void printDistribution(OutputStream outputStream){

        PrintWriter printWriter = new PrintWriter(outputStream);

        int [] gradeDistribution = new int[6];
        for(Student s : students){
            gradeDistribution[s.getGrade() - 'A']++;
        }
        for(int i=0;i<6;i++){

            printWriter.printf("%c -> %d\n",i + 'A',  gradeDistribution[i]);
        }

        printWriter.flush();
    }
}

public class GradesTest {
    public static void main(String[] args) {

        Course course = new Course();
        File inputFile = new File("C:\\Users\\Sadstro\\Desktop\\FINKI\\NP2021\\src\\students");
        File outputFile = new File("C:\\Users\\Sadstro\\Desktop\\FINKI\\NP2021\\src\\results");
        try {
            course.readData(new FileInputStream(inputFile));
            System.out.println("===Printing sorted students to screen===");
            course.printSortedData(System.out);

            System.out.println("===Printing detailed report to file===");
            course.printDetailedData(new FileOutputStream(outputFile));

            System.out.println("===Printing grade distribution to screen===");
            course.printDistribution(System.out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
