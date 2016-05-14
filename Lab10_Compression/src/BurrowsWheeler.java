import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int R = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String msgToEncode = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(msgToEncode);
        int first = 0;
        for (int i = 0; i < csa.length(); ++i) {
            if (csa.index(i) == 0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < csa.length(); ++i) {
            int end = (msgToEncode.length() - 1 + csa.index(i)) % msgToEncode.length();
            BinaryStdOut.write(msgToEncode.charAt(end));
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int pointer = BinaryStdIn.readInt();
        String enc = BinaryStdIn.readString();
        char[] sorted = new char[enc.length()];
        int[] next = new int[enc.length()];
        int[] count = new int[R + 1];
        for (int i = 0; i < enc.length(); ++i) {
            count[enc.charAt(i) + 1]++;
        }
        for (int r = 0; r < R; ++r) {
            count[r+1] += count[r];
        }
        for (int i = 0; i < enc.length(); ++i) {
            int skip = count[enc.charAt(i)]++;
            sorted[skip] = enc.charAt(i);
            next[skip] = i;
        }
        for (int i = 0; i < enc.length(); ++i) {
            BinaryStdOut.write(sorted[pointer]);
            pointer = next[pointer];
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
    }
}