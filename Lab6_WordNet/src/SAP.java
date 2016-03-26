import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

import static sun.text.bidi.BidiBase.MIXED;


public class SAP {

    private final Digraph digraph;
    private final int V;

    private class BFS {

        private final static byte BLUE = 1;
        private final static byte CYAN = 2;
        private final static byte MIXED = BLUE | CYAN;

        private int shortestDistance = Integer.MAX_VALUE;
        private int ancestor = -1;

        private boolean opposite(final byte c1, final byte c2) {
            return (c1 | c2) == MIXED;
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

        private void verifyNotEmpty(Iterable<Integer> vertices) {
            Objects.requireNonNull(vertices);
            if (!vertices.iterator().hasNext()) {
                throw new IllegalArgumentException();
            }
        }

        private BFS(Iterable<Integer> vFrom, Iterable<Integer> vTo) {
            verifyNotEmpty(vFrom);
            verifyNotEmpty(vTo);
            if (intersects(vFrom, vTo)) {
                shortestDistance = 0;
                ancestor = vFrom.iterator().next();
                return;
            }
            Queue<Integer> queue = new Queue<>();
            byte[] color = new byte[V];
            int[] distance = new int[V];
            for (int vertex = 0; vertex < V; ++vertex) {
                distance[vertex] = Integer.MAX_VALUE;
            }
            for (int va : vFrom) {
                queue.enqueue(va);
                color[va] = CYAN;
                distance[va] = 0;
            }
            for (int vb : vTo) {
                queue.enqueue(vb);
                color[vb] = BLUE;
                distance[vb] = 0;
            }
            while (!queue.isEmpty()) {
                int vertex = queue.dequeue();
                for (int vi : digraph.adj(vertex)) {
                    if (color[vi] == 0) {
                        color[vi] = color[vertex];
                        distance[vi] = distance[vertex] + 1;
                        queue.enqueue(vi);
                    }
                    else if (opposite(color[vertex], color[vi])) {
                        color[vi] = MIXED;
                        distance[vi] = distance[vertex] + 1;
                    }
                }
            }
            for (int vertex = 0; vertex < V; ++vertex) {
                if (color[vertex] == MIXED && distance[vertex] < shortestDistance) {
                    shortestDistance = distance[vertex];
                    ancestor = vertex;
                }
            }
            if (shortestDistance == Integer.MAX_VALUE) {
                shortestDistance = -1;
            }
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        Objects.requireNonNull(G);
        V = G.V();
        digraph = new Digraph(V);
        for (int vertex = 0; vertex < V; ++vertex) {
            for (int w : G.adj(vertex)) {
                digraph.addEdge(vertex, w);
            }
        }
    }

    // length of shortest ancestral path between v and w;
    // -1 if no such path
    public int length(int v, int w) {
        Stack<Integer> vIterable = new Stack<>();
        Stack<Integer> wIterable = new Stack<>();
        vIterable.push(v);
        wIterable.push(w);
        return length(vIterable, wIterable);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        Stack<Integer> vIterable = new Stack<>();
        Stack<Integer> wIterable = new Stack<>();
        vIterable.push(v);
        wIterable.push(w);
        return ancestor(vIterable, wIterable);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return new BFS(v, w).shortestDistance;
    }

    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return new BFS(v, w).ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
