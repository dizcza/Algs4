public class CircularSuffixArray {
    private final int index[];

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        index = QuickCircularSort.sort(s);
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i)               {
        return index[i];
    }


    public static void main(String[] args) {

    }
}
