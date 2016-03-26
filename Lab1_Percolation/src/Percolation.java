import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int sinkId;
    private final int sourceId;
    private final int N;

    private boolean[] isCellOpened;

    private final WeightedQuickUnionUF weightedQuickUnion;

    /**
     * Creates N-by-N grid, with all sites blocked
     *
     * @param N grid size
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }

        this.N = N;
        sinkId = N * N;
        sourceId = N * N + 1;

        weightedQuickUnion = new WeightedQuickUnionUF(N * N + 2);

        for (int col = 1; col <= N; ++col) {
            weightedQuickUnion.union(xyToId(1, col), sinkId);
            weightedQuickUnion.union(xyToId(N, col), sourceId);
        }

        isCellOpened = new boolean[N * N + 2];
    }

    /**
     * @param row
     * @param col
     * @return 0 .. N^2-1
     */
    private int xyToId(final int row, final int col) {
        return N * (row - 1) + col - 1;
    }

    private void bindWithNeighbors(int cellId) {
        if (cellId - N >= 0 && isCellOpened[cellId - N]) {
            // top
            weightedQuickUnion.union(cellId - N, cellId);
        }
        if (cellId + N < N * N && isCellOpened[cellId + N]) {
            // bottom
            weightedQuickUnion.union(cellId + N, cellId);
        }
        final int column = cellId % N;
        if (column - 1 >= 0 && isCellOpened[cellId - 1]) {
            // left
            weightedQuickUnion.union(cellId - 1, cellId);
        }
        if (column + 1 < N && isCellOpened[cellId + 1]) {
            // right
            weightedQuickUnion.union(cellId + 1, cellId);
        }
    }

    /**
     * Opens a site (row i, column j) if it is not open already
     *
     * @param i row
     * @param j column
     */
    public void open(int i, int j) {
        validateIndices(i, j);
        final int flattenId = xyToId(i, j);
        if (!isCellOpened[flattenId]) {
            isCellOpened[flattenId] = true;
            bindWithNeighbors(flattenId);
        }
    }

    /**
     * Is site (row i, column j) open?
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isOpen(int i, int j) {
        validateIndices(i, j);
        return isCellOpened[xyToId(i, j)];
    }

    /**
     * Is site (row i, column j) full?
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isFull(int i, int j) {
        validateIndices(i, j);
        return isOpen(i, j) && weightedQuickUnion.connected(sinkId, xyToId(i, j));
    }

    /**
     * does the system percolate?
     *
     * @return
     */
    public boolean percolates() {
        if (N == 1) {
            return isFull(1, 1);
        }
        else {
            return weightedQuickUnion.connected(sinkId, sourceId);
        }
    }

    private void validateIndices(final int row, final int col) {
        if (row <= 0 || row > N || col <= 0 || col > N) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * test client (optional)
     *
     * @param args
     */
    public static void main(String[] args) {
    }
}