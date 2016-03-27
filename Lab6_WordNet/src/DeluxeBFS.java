import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;


/**
 * Created by dizcza on 26.03.16.
 */
public class DeluxeBFS {
    private final Digraph digraph;
    private final int V;

    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path

    public boolean hasPathTo(int vertex) {
        return marked[vertex];
    }

    public int distTo(int vertex) {
        return distTo[vertex];
    }

    public DeluxeBFS(Digraph digraph, Iterable<Integer> sources) {
        this.digraph = digraph;
        V = digraph.V();
        marked = new boolean[V];
        distTo = new int[V];
        bfs(sources);
    }

    private void bfs(Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            verifyBounds(s);
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : digraph.adj(v)) {
                if (!marked[w]) {
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    private void verifyBounds(int vertex) {
        if (vertex < 0 || vertex >= V) {
            throw new IndexOutOfBoundsException();
        }
    }

}
