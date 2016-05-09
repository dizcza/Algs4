import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

/**
 *  The <tt>Quick3string</tt> class provides static methods for sorting an
 *  array of strings using 3-way radix quicksort.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/51radix">Section 5.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class QuickCircularSort {
    private static final int CUTOFF =  15;   // cutoff to insertion sort

    // do not instantiate
    private QuickCircularSort() { }

    /**
     * Rearranges the array of strings in ascending order.
     *
     * @param a the array to be sorted
     */
    public static int[] sort(String a) {
        int[] index = new int[a.length()];
        for (int i = 0; i < a.length(); ++i) {
            index[i] = i;
        }
        sort(a, index, 0, a.length()-1, 0);
        printSuffixes(a, index);
//        a = "ABRACADABRA!";
//        index = new int[] {11, 10, 7, 0, 3, 5, 8, 1, 4, 6, 9, 2};
        assert isSorted(a, index);
        return index;
    }

    // return the dth character of s, -1 if d = length of s
    private static int charAt(String s, int d) {
        assert d >= 0;
        return s.charAt(d % s.length());
    }


    // 3-way string quicksort a[lo..hi] starting at dth character
    private static void sort(String a, int[] index, int lo, int hi, int d) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, index, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
//        int v = charAt(a[lo], d);
//        int i = lo + 1;
//        while (i <= gt) {
//            int t = charAt(a[i], d);
//            if      (t < v) exch(a, lt++, i++);
//            else if (t > v) exch(a, i, gt--);
//            else              i++;
//        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
//        sort(a, lo, lt-1, d);
        if (d < a.length() - 1) sort(a, index, lt, gt, d+1);
//        sort(a, gt+1, hi, d);
    }

    private static void printLine(String a, int[] index, int line) {
        for (int i = 0; i < a.length(); ++i) {
            System.out.print((char) charAt(a, index[line]+i));
            System.out.print(' ');
        }
        System.out.print('\n');
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private static void insertion(String a, int[] index, int lo, int hi, int d0) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo; --j) {
                int d = getFirstNotEqualCharacter(a, index, j, d0);
                if (d == a.length()) break;
                if (charAt(a, index[j] + d) < charAt(a, index[j-1] + d)) {
                    System.out.println(String.format("index[j=%d]=%d, d=%d", j, index[j], d));
                    printSuffixesBeforeSwap(a, index, j);
                    exch(index, j, j - 1);
                }
            }
        }
    }

    // exchange a[i] and a[j]
    private static void exch(int[] index, int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }

    // is v less than w, starting at character d
    // DEPRECATED BECAUSE OF SLOW SUBSTRING EXTRACTION IN JAVA 7
    // private static boolean less(String v, String w, int d) {
    //    assert v.substring(0, d).equals(w.substring(0, d));
    //    return v.substring(d).compareTo(w.substring(d)) < 0; 
    // }

    // is v less than w, starting at character d
    private static boolean less(String v, String w, int d) {
        assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return v.length() < w.length();
    }

    private static int getFirstNotEqualCharacter(String a, int[] index, int line, int d0) {
        int d = d0;
        while (d < a.length() && charAt(a, index[line-1] + d) == charAt(a, index[line] + d)) {
            d++;
        }
        return d;
    }

    // is the array sorted
    private static boolean isSorted(String a, int[] index) {
        for (int i = 1; i < a.length(); i++) {
            int d = getFirstNotEqualCharacter(a, index, i, 0);
            if (d < a.length() && a.charAt(index[i-1] + d) > a.charAt(index[i] + d)) return false;
        }
        return true;
    }

    private static void printSuffixes(String a, int[] index) {
        for (int i = 0; i < a.length(); ++i) {
            for (int j = 0; j < a.length(); ++j) {
                System.out.print((char) charAt(a, i+j));
                System.out.print(' ');
            }
            System.out.print(" |  ");
            printLine(a, index, i);
        }
    }

    private static void printSuffixesBeforeSwap(String a, int[] index, int line) {
        for (int i = 0; i < a.length(); ++i) {
            for (int j = 0; j < a.length(); ++j) {
                System.out.print((char) charAt(a, i+j));
                System.out.print(' ');
            }
            System.out.print(" |  ");
            if (i == line || i == line-1) {
                System.out.print('*');
            }
            else {
                System.out.print(' ');
            }
            printLine(a, index, i);
        }
    }


    /**
     * Reads in a sequence of fixed-length strings from standard input;
     * 3-way radix quicksorts them;
     * and prints them to standard output in ascending order.
     */
    public static void main(String[] args) {

        // read in the strings from standard input
//        String a = StdIn.readString();
        String a = "ABRACADABRA!";

        // sort the strings
        int[] index = sort(a);
        System.out.print('\n');
        printSuffixes(a, index);
        System.out.println(Arrays.toString(index));
    }
}