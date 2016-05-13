import java.util.Objects;

public class CircularSuffixArray {
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        Objects.requireNonNull(s);
        index = QuickCircularSort.sort(s);
    }

    private void checkBounds(int i) {
        if (i < 0 || i >= index.length) {
            throw new IndexOutOfBoundsException();
        }
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        checkBounds(i);
        return index[i];
    }


    public static void main(String[] args) {

    }
}
