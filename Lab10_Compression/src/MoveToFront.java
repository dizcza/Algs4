import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

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
        for (int i = 0; i < alph.size(); ++i) {
            if (alph.get(i) == ch) return i;
        }
        return -1;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> alph = getAlphabet();
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            int pos = getPos(alph, ch);
            BinaryStdOut.write(pos, 8);
            alph.addFirst(ch);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> alph = getAlphabet();
        int i = 0;
        while (!BinaryStdIn.isEmpty()) {
            int pos = BinaryStdIn.readChar();
            char ch = alph.remove(pos);
//            System.out.print(ch);
            BinaryStdOut.write(ch);
            alph.addFirst(ch);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static void testDecode() {
        LinkedList<Character> alph = new LinkedList<>();
        for (int r = 0; r < 6; ++r) {
            alph.addLast((char) (r + 'A'));
        }
        while (!BinaryStdIn.isEmpty()) {
            int pos = BinaryStdIn.readChar();
            char ch = alph.remove(pos);
            BinaryStdOut.write(ch);
            alph.addFirst(ch);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static void testEncode(boolean verbose) {
        LinkedList<Character> alph = new LinkedList<>();
        for (int r = 0; r < 6; ++r) {
            alph.addLast((char) (r + 'A'));
        }
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            if (ch == '\n') break;
            int pos = getPos(alph, ch);
            if (verbose) {
                System.out.println(alph.toString() + '\t' + ch + "  " + pos);
            }
            else {
                BinaryStdOut.write(pos, 8);
            }
            alph.remove(pos);
            alph.addFirst(ch);
        }
        if (verbose) {
            System.out.println(alph.toString());
        }
        else {
            BinaryStdOut.flush();
            BinaryStdOut.close();
        }
    }


    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            testEncode(false);
        }
        else if (args[0].equals("+")) {
            testDecode();
        }
    }
}
