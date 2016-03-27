import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Objects;
import java.util.Stack;


public class SAP {

    private final DeluxeBFS bfs;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        Objects.requireNonNull(G);
        int size = G.V();
        Digraph digraph = new Digraph(size);
        for (int vertex = 0; vertex < size; ++vertex) {
            for (int w : G.adj(vertex)) {
                digraph.addEdge(vertex, w);
            }
        }
        bfs = new DeluxeBFS(digraph);
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
        return bfs.length(v, w);
    }

    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs.ancestor(v, w);
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
