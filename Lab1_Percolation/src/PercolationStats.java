import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] probabilities;
    private final int T;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        this.T = T;
        final int cellsTotal = N * N;

        probabilities = new double[T];
        for (int testCase = 0; testCase < T; ++testCase) {
            Percolation percolationModel = new Percolation(N);
            int kicks = 0;
            while (!percolationModel.percolates()) {
                int x = StdRandom.uniform(1, N+1);
                int y = StdRandom.uniform(1, N+1);
                if (!percolationModel.isOpen(x, y)) {
                    percolationModel.open(x, y);
                    ++kicks;
                }
            }
            probabilities[testCase] = ((double) kicks) / cellsTotal;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(probabilities);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (T > 1) {
            return StdStats.stddev(probabilities);
        }
        else {
            return Double.NaN;
        }
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    // test client (described below)
    public static void main(String[] args) {
        final int N, T;
        if (args.length == 2) {
            N = Integer.parseInt(args[0]);
            T = Integer.parseInt(args[1]);
        }
        else {
            N = 500;
            T = 200;
        }
        PercolationStats percolationStats = new PercolationStats(N, T);
        System.out.println("mean: " + percolationStats.mean());
        System.out.println("stddev: " + percolationStats.stddev());
        double confLo = percolationStats.confidenceLo();
        double confHi = percolationStats.confidenceHi();
        System.out.println("95% confidence interval: " + confLo + ", " + confHi);
    }
}