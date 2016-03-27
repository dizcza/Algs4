import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

/**
 * Created by dizcza on 27.03.16.
 */
public class DepthFirstSearchCycle {
    private final static byte YELLOW = 1;
    private final static byte GREEN = 2;

    private final byte[] marked;
    private int root = -1;

    /**
     * Checks whether <tt>digraph</tt> is a rooted DAG.
     */
    public DepthFirstSearchCycle(Digraph digraph) {
        marked = new byte[digraph.V()];
        for (int vertex = 0; vertex < digraph.V(); ++vertex) {
            if (marked[vertex] == 0) {
                checkRoot(digraph, vertex);
                dfs(digraph, vertex);
            }
        }
    }

    private void checkRoot(Digraph digraph, int vertex) {
        if (digraph.outdegree(vertex) == 0) {
            if (root == -1) {
                root = vertex;
            }
            else {
                throw new IllegalArgumentException("Multiple roots detected");
            }
        }
    }

    // depth first search from v
    private void dfs(Digraph digraph, int sink) {
        marked[sink] = YELLOW;
        for (int w : digraph.adj(sink)) {
            if (marked[w] == 0) {
                marked[w] = YELLOW;
                checkRoot(digraph, w);
                dfs(digraph, w);
            }
            else if (marked[w] == YELLOW) {
                throw new IllegalArgumentException("Cycle is detected.");
            }
            else {
            }
        }
        marked[sink] = GREEN;
    }

    /**
     * Unit tests the <tt>DepthFirstSearch</tt> data type.
     */
    public static void main(String[] args) {
        In in = new In("Lab6_WordNet/testing/digraph1.txt");
        Digraph G = new Digraph(in);
        DepthFirstSearchCycle search = new DepthFirstSearchCycle(G);
    }
}
