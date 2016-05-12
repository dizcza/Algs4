import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

import static javax.swing.UIManager.get;

public class MoveToFront {

    private final static int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Integer> alph = new LinkedList<>();
        for (int r = 0; r < R; ++r) {
            alph.addLast(r);
        }
        while (!BinaryStdIn.isEmpty()) {
            int c = (int) BinaryStdIn.readChar();
            int pos = alph.remove(c);
            BinaryStdOut.write(pos);
            alph.addFirst(c);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
    }
}
