import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Iterator;
import java.util.LinkedList;

public class MoveToFront {

    private final static int R = 256;

    private static LinkedList<Character> getAlphabet() {
        LinkedList<Character> alph = new LinkedList<>();
        for (int r = 0; r < R; ++r) {
            alph.addLast((char) r);
        }
        return alph;
    }

    private static int getPos(LinkedList<Character> alph, char ch) {
        Iterator<Character> it = alph.iterator();
        for (int i = 0; it.hasNext(); ++i) {
            if (it.next() == ch) return i;
        }
        return -1;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        encode(getAlphabet());
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        decode(getAlphabet());
    }

    private static void encode(LinkedList<Character> alph) {
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            int pos = getPos(alph, ch);
            alph.remove(pos);
            BinaryStdOut.write(pos, 8);
            alph.addFirst(ch);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static void decode(LinkedList<Character> alph) {
        while (!BinaryStdIn.isEmpty()) {
            int pos = BinaryStdIn.readChar();
            char ch = alph.remove(pos);
            BinaryStdOut.write(ch);
            alph.addFirst(ch);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static void testEncode() {
        LinkedList<Character> alph = new LinkedList<>();
        for (int r = 0; r < 6; ++r) {
            alph.addLast((char) (r + 'A'));
        }
        encode(alph);
    }

    private static void testDecode() {
        LinkedList<Character> alph = new LinkedList<>();
        for (int r = 0; r < 6; ++r) {
            alph.addLast((char) (r + 'A'));
        }
        decode(alph);
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
