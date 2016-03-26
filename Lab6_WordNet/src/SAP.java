import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;


public class SAP {

    private final Digraph digraph;
    private final int V;

    private class BFS {

        private final static byte RED = 1;
        private final static byte BLUE = 2;

        private final static int INFINITY = Integer.MAX_VALUE;

        private int shortestDistance = INFINITY;
        private int ancestor = -1;

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
            Map<Byte, Integer[]> paintedDistance = new HashMap<>();
            paintedDistance.put(RED, new Integer[V]);
            paintedDistance.put(BLUE, new Integer[V]);
            for (int vertex = 0; vertex < V; ++vertex) {
                paintedDistance.get(RED)[vertex] = INFINITY;
                paintedDistance.get(BLUE)[vertex] = INFINITY;
            }
            for (int va : vFrom) {
                queue.enqueue(va);
                color[va] = RED;
                paintedDistance.get(RED)[va] = 0;
            }
            for (int vb : vTo) {
                queue.enqueue(vb);
                color[vb] = BLUE;
                paintedDistance.get(BLUE)[vb] = 0;
            }
            while (!queue.isEmpty()) {
                int vertex = queue.dequeue();
                for (int vi : digraph.adj(vertex)) {
                    Integer[] dist = paintedDistance.get(color[vertex]);
                    dist[vi] = dist[vertex] + 1;
                    if (color[vi] == 0) {
                        color[vi] = color[vertex];
                        queue.enqueue(vi);
                    }
                }
            }
            for (int vertex = 0; vertex < V; ++vertex) {
                int d1 = paintedDistance.get(RED)[vertex];
                int d2 = paintedDistance.get(BLUE)[vertex];
                if (d1 != INFINITY && d2 != INFINITY && d1 + d2 < shortestDistance) {
                    shortestDistance = d1 + d2;
                    ancestor = vertex;
                }
            }
            if (shortestDistance == INFINITY) {
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
        In in = new In("Lab6_WordNet/wordnet/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
