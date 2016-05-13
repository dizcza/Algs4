import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by dizcza on 12.05.16.
 */
public class TestStd {

    private static void writeToStdOut() {
        char[] arr = new char[10];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = (char) ('A' + i);
        }
        for (int int8val : arr) {
            BinaryStdOut.write(int8val, 8);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static void printFromStdIn() {
        while (!BinaryStdIn.isEmpty()) {
            int int8val = BinaryStdIn.readChar();
            System.out.print(int8val - 'A');
        }
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            writeToStdOut();
        }
        else if (args[0].equals("+")) {
            printFromStdIn();
        }
    }
}
