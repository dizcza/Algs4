import edu.princeton.cs.algs4.StdIn;

public class Subset {
    public static void main(String[] args) {
        final int k = Integer.parseInt(args[0]);
        final RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        for (int iter = 0; iter < k; ++iter) {
            System.out.println(randomizedQueue.dequeue());
        }
    }
}
