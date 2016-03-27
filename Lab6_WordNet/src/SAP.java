import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Objects;
import java.util.Stack;


public class SAP {

    private final Digraph digraph;
    private final int V;

    private static class Ancestor {
        private final int vertex;
        private final int length;

        private Ancestor(int vertex, int length) {
            this.vertex = vertex;
            this.length = length;
        }

        @Override
        public int hashCode() {
            return vertex + length;
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

    private Ancestor doubleBfs(Iterable<Integer> v, Iterable<Integer> w) {
        DeluxeBFS bfsFrom = new DeluxeBFS(digraph, v);
        DeluxeBFS bfsTo = new DeluxeBFS(digraph, w);

        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int vertex = 0; vertex < V; ++vertex) {
            if (bfsFrom.hasPathTo(vertex) && bfsTo.hasPathTo(vertex)) {
                int vDist = bfsFrom.distTo(vertex) + bfsTo.distTo(vertex);
                if (vDist < length) {
                    length = vDist;
                    ancestor = vertex;
                }
            }
        }

        if (length == Integer.MAX_VALUE) {
            length = -1;
        }

        return new Ancestor(ancestor, length);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return doubleBfs(v, w).length;
    }

    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return doubleBfs(v, w).vertex;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in;
        if (args.length > 0) {
            in = new In("../testing/" + args[0]);
        }
        else {
            in = new In("Lab6_WordNet/testing/digraph6.txt");
        }
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
