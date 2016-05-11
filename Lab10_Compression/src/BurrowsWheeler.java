import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdIn;

public class BurrowsWheeler {

    private final static int R = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String a = StdIn.readString();
        int index[] = QuickCircularSort.sort(a);
        int first = 0;
        for (int i = 0; i < index.length; ++i) {
            if (index[i] == 0) {
                first = i;
                break;
            }
        }
        System.out.println(first);
        for (int ai : index) {
            int end = (a.length() - 1 + ai) % a.length();
            System.out.print(a.charAt(end));
        }
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int pointer = StdIn.readInt();
        String enc = StdIn.readString();
        char sorted[] = new char[enc.length()];
        int next[] = new int[enc.length()];
        int count[] = new int[R + 1];
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
            System.out.print(sorted[pointer]);
            pointer = next[pointer];
        }
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