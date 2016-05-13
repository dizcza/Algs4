import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Iterator;

public class MoveToFront {

    private static final int R = 256;

    private static LinkedNodeList<Character> getAlphabet() {
        LinkedNodeList<Character> alph = new LinkedNodeList<>();
        for (int r = 0; r < R; ++r) {
            alph.addLast((char) r);
        }
        return alph;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        encode(getAlphabet());
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        decode(getAlphabet());
    }

    private static void encode(LinkedNodeList<Character> alph) {
        while (!BinaryStdIn.isEmpty()) {
            char readChar = BinaryStdIn.readChar();
            LinkedNodeList<Character>.Node<Character> node = null;
            Iterator<LinkedNodeList<Character>.Node<Character>> it = alph.iterator();
            int pos = 0;
            while (it.hasNext()) {
                node = it.next();
                if (node.item == readChar) break;
                pos++;
            }
            alph.remove(node);
            BinaryStdOut.write(pos, 8);
            alph.addFirst(readChar);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static void decode(LinkedNodeList<Character> alph) {
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
        LinkedNodeList<Character> alph = new LinkedNodeList<>();
        for (int r = 0; r < 6; ++r) {
            alph.addLast((char) (r + 'A'));
        }
        encode(alph);
    }

    private static void testDecode() {
        LinkedNodeList<Character> alph = new LinkedNodeList<>();
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
