import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Created by dizcza on 26.03.16.
 */
public class DeluxeBFS {
    private final static int INFINITY = Integer.MAX_VALUE;
    private final static byte RED = 1;
    private final static byte BLUE = 2;

    private final Ancestor invalidAncestor = new Ancestor(-1, -1);
    private final Digraph digraph;
    private final int V;

    private static class Ancestor {
        private final int vertex;
        private final int length;

        private Ancestor(int vertex, int length) {
            this.vertex = vertex;
            this.length = length;
        }
    }

    public DeluxeBFS(Digraph digraph) {
        this.digraph = digraph;
        V = digraph.V();
    }

    private void verifyBounds(int vertex) {
        if (vertex < 0 || vertex >= V) {
            throw new IndexOutOfBoundsException();
        }
    }

    private boolean intersects(Iterable<Integer> vFrom, Iterable<Integer> vTo) {
        Set<Integer> patch = new HashSet<>();
        for (int va : vFrom) {
            verifyBounds(va);
            patch.add(va);
        }
        for (int vb : vTo) {
            verifyBounds(vb);
            if (patch.contains(vb)) {
                return true;
            }
        }
        return false;
    }

    private Ancestor findAncestor(Iterable<Integer> vFrom, Iterable<Integer> vTo) {
        Objects.requireNonNull(vFrom);
        Objects.requireNonNull(vTo);
        if (!vFrom.iterator().hasNext() || !vTo.iterator().hasNext()) {
            return invalidAncestor;
        }
        int length = INFINITY;
        int ancestorV = -1;
        if (intersects(vFrom, vTo)) {
            return new Ancestor(vFrom.iterator().next(), 0);
        }
        Queue<Integer> queue = new Queue<>();
        byte[] color = new byte[V];
        Map<Byte, Integer[]> paintedDistance = new HashMap<>();
        paintedDistance.put(RED, new Integer[V]);
        paintedDistance.put(BLUE, new Integer[V]);
        for (int vertex = 0; vertex < V; ++vertex) {
            paintedDistance.get(RED)[vertex] = INFINITY;
            paintedDistance.get(BLUE)[vertex] = INFINITY;
        }
        Map<Byte, Boolean[]> marked = new HashMap<>();
        marked.put(RED, new Boolean[V]);
        marked.put(BLUE, new Boolean[V]);
        for (int va : vFrom) {
            queue.enqueue(va);
            color[va] = RED;
            paintedDistance.get(RED)[va] = 0;
            marked.get(RED)[va] = true;
        }
        for (int vb : vTo) {
            queue.enqueue(vb);
            color[vb] = BLUE;
            paintedDistance.get(BLUE)[vb] = 0;
            marked.get(BLUE)[vb] = true;
        }
        while (!queue.isEmpty()) {
            int vertex = queue.dequeue();
            for (int vi : digraph.adj(vertex)) {
                Integer[] dist = paintedDistance.get(color[vertex]);
                if (dist[vertex] + 1 < dist[vi]) {
                    dist[vi] = dist[vertex] + 1;
                }
                if (color[vi] == 0) {
                    // todo: check d1 + d2 < distSoFar here
                    color[vi] = color[vertex];
                    queue.enqueue(vi);
                }
            }
        }
        for (int vertex = 0; vertex < V; ++vertex) {
            int d1 = paintedDistance.get(RED)[vertex];
            int d2 = paintedDistance.get(BLUE)[vertex];
            if (d1 != INFINITY && d2 != INFINITY && d1 + d2 < length) {
                length = d1 + d2;
                ancestorV = vertex;
            }
        }
        if (length == INFINITY) {
            length = -1;
        }
        return new Ancestor(ancestorV, length);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return findAncestor(v, w).length;
    }

    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return findAncestor(v, w).vertex;
    }
}
