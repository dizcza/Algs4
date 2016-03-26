import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by dizcza on 01.02.16.
 */
public class MyTest {

    private static final int DELAY = 100;

    public static void main(String[] args) {
        In in = new In("/home/dizcza/IdeaProjects/Percolation/testing/input20.txt");
        int N = in.readInt();         // N-by-N percolation system

        // turn on animation mode
        StdDraw.show(0);

        // repeatedly read in sites to open and draw resulting system
        Percolation perc = new Percolation(N);
        PercolationVisualizer.draw(perc, N);
        StdDraw.show(DELAY);
        for (int step=0; step < 231; ++step) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            PercolationVisualizer.draw(perc, N);
            StdDraw.show(DELAY);
        }
    }
}
