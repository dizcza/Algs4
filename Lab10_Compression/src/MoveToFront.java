import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdIn;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            System.out.print(c);
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        encode();
    }
}
