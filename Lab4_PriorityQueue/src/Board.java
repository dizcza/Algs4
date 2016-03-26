import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Stack;

public class Board {

    private final int[][] tiles;
    private final int N;
    private int manh = -1;

    // construct a board from an N-by-N array of tiles
    // (where tiles[i][j] = block in col i, rowumn j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new NullPointerException();
        }
        N = blocks.length;
        tiles = copy2d(blocks);
    }

    private int[][] copy2d(final int[][] blocks) {
        final int[][] tilesCopy = new int[N][N];
        for (int row = 0; row < N; ++row) {
            tilesCopy[row] = Arrays.copyOf(blocks[row], N);
        }
        return tilesCopy;
    }

    // board dimension N
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        int wrong = 0;
        for (int row = 0; row < N; ++row) {
            for (int col = 0; col < N; ++col) {
                int goalValue = N * row + col + 1;
                if (tiles[row][col] != 0 && tiles[row][col] != goalValue) {
                    wrong++;
                }
            }
        }

        return wrong;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manh != -1) return manh;
        int dist = 0;
        for (int row = 0; row < N; ++row) {
            for (int col = 0; col < N; ++col) {
                final int num = tiles[row][col];
                if (num != 0) {
                    int xGoal = (num - 1) / N;
                    int yGoal = (num - 1) % N;
                    dist += Math.abs(xGoal - row) + Math.abs(yGoal - col);
                }
            }
        }
        manh = dist;
        return dist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int row = 0; row < N; ++row) {
            for (int col = 0; col < N; ++col) {
                final int goalValue;
                if (row == N - 1  && col == N - 1) {
                    goalValue = 0;
                }
                else {
                    goalValue = N * row + col + 1;
                }
                if (tiles[row][col] != goalValue) {
                    return false;
                }
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        exchangeFirstKeys(); // exchange (0,0) with (0,1)
        Board twinBoard = new Board(tiles);
        exchangeFirstKeys(); // roll it back
        return twinBoard;
    }

    private void exchangeFirstKeys() {
        int x0 = 0, y0 = 0;
        int x1 = 0, y1 = 1;
        if (tiles[x0][y0] == 0) {
            x0 = 1;
        }
        else if(tiles[x1][y1] == 0) {
            x1 = 1;
            y1 = 0;
        }
        exchange(x0, y0, x1, y1);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board other = (Board) y;
        if (this.N != other.N) return false;
        for (int row = 0; row < N; ++row) {
            for (int col = 0; col < N; ++col) {
                if (this.tiles[row][col] != other.tiles[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void exchange(int x0, int y0, int x1, int y1) {
        int prev0 = tiles[x0][y0];
        tiles[x0][y0] = tiles[x1][y1];
        tiles[x1][y1] = prev0;
    }

    private int[] getEmptyBlockPos() {
        for (int row = 0; row < N; ++row) {
            for (int col = 0; col < N; ++col) {
                if (tiles[row][col] == 0) {
                    return new int[] {row, col};
                }
            }
        }
        throw new NoSuchElementException("No empty block is found");
    }

    private void insertNeighborInto(Stack<Board> boards,
                                    final int xEmpty, final int yEmpty,
                                    final int xTo, final int yTo) {
        // move empty block
        exchange(xEmpty, yEmpty, xTo, yTo);

        Board child = new Board(tiles);
        boards.add(child);

        // roll back
        exchange(xEmpty, yEmpty, xTo, yTo);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        final Stack<Board> neighborBoards = new Stack<>();
        int[] emptyPos = getEmptyBlockPos();
        int xEmpty = emptyPos[0];
        int yEmpty = emptyPos[1];

        if (xEmpty > 0) {
            insertNeighborInto(neighborBoards, xEmpty, yEmpty, xEmpty - 1, yEmpty);
        }
        if (xEmpty < N - 1) {
            insertNeighborInto(neighborBoards, xEmpty, yEmpty, xEmpty + 1, yEmpty);
        }
        if (yEmpty > 0) {
            insertNeighborInto(neighborBoards, xEmpty, yEmpty, xEmpty, yEmpty - 1);
        }
        if (yEmpty < N - 1) {
            insertNeighborInto(neighborBoards, xEmpty, yEmpty, xEmpty, yEmpty + 1);
        }

        return neighborBoards;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {

    }

}