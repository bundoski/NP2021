package lab2;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
class InsufficientElementsException extends Exception{

    public InsufficientElementsException() {
        super("Insufficient number of elements");
    }

    public InsufficientElementsException(String message) {
        super(message);
    }
}

class InvalidRowNumberException extends Exception{

    public InvalidRowNumberException() {
        super("Invalid row number");
    }

    public InvalidRowNumberException(String message) {
        super(message);
    }
}

class InvalidColumnNumberException  extends Exception{

    public InvalidColumnNumberException () {
        super("Invalid column number");
    }

    public InvalidColumnNumberException (String message) {
        super(message);
    }
}

class MatrixReader{
    private static int m,n;
    private static double[][] a;

    public static DoubleMatrix read(InputStream input){
        Scanner in = new Scanner(input);
        m=in.nextInt();
        n=in.nextInt();
        a = new double[m][n];
        for(int i=0;i<m;i++)
            for(int j=0;j<n;j++){
                a[i][j]=in.nextDouble();
            }
        DoubleMatrix matrix=new DoubleMatrix(a,m,n);

        return matrix;
    }
}

final class DoubleMatrix{
    private double[][] matrix;
    private int m,n;

    public DoubleMatrix(double[][] a,int m, int n){
        matrix=new double[m][n];
        matrix=a;
        this.m=m;
        this.n=n;
    }

    public DoubleMatrix(double a[], int m, int n) throws InsufficientElementsException {
        matrix = new double[m][n];
        if (a.length < (m * n)) throw new InsufficientElementsException();

        if(a.length==m*n){
            int br=0;
            for(int i=0;i<m;i++)
                for(int j=0;j<n;j++) {
                    matrix[i][j] = a[br];
                    br++;
                }
        }
        if(a.length>(m*n)){
            int br=a.length-m*n;
            for(int i=0;i<m;i++)
                for(int j=0;j<n;j++) {
                    matrix[i][j] = a[br];
                    br++;
                }
        }
        this.m=m;
        this.n=n;
    }

    String getDimensions(){
        return "["+m+" x "+n+"]";
    }
    int rows(){ return m; }
    int columns(){ return n; }

    public double[][] getMatrix() {
        return matrix;
    }

    double maxElementAtRow(int row) throws InvalidRowNumberException {
        if (row > m || row < 1) throw new InvalidRowNumberException();
        else {
            double max = -10000;
            for (int i=0;i<n;i++) {
                if (max < matrix[row-1][i]) max = matrix[row-1][i];
            }
            return max;
        }
    }

    double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if (column > n || column < 1) throw new InvalidColumnNumberException();
        else {
            double max = -10000;
            for (int i=0;i<m;i++) {
                if (max < matrix[i][column-1]) max = matrix[i][column-1];
            }
            return max;
        }
    }

    double sum(){
        double suma=0;
        for (int i=0;i<m;i++)
            for(int j=0;j<n;j++)
                suma+=matrix[i][j];
        return suma;
    }

    double[] toSortedArray(){
        double[] niza=new double[m*n];
        int br=0;
        for (int i=0;i<m;i++)
            for(int j=0;j<n;j++) {
                niza[br] = matrix[i][j];
                br++;
            }
        Arrays.sort(niza);
        for(int i = 0; i < niza.length / 2; i++)
        {
            double temp = niza[i];
            niza[i] = niza[niza.length - i - 1];
            niza[niza.length - i - 1] = temp;
        }

        return niza;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++) {
                sb.append(df.format(matrix[i][j]));
                if(j!=n-1) sb.append("\t");
            }
            if (i!=m-1) sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleMatrix)) return false;

        DoubleMatrix that = (DoubleMatrix) o;

        if (m != that.m) return false;
        if (n != that.n) return false;
        return Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(matrix);
        result = 31 * result + m;
        result = 31 * result + n;
        return result;
    }


}

public class DoubleMatrixTester {


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode()&&f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }

}