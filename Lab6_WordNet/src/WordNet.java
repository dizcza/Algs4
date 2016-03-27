import edu.princeton.cs.algs4.DepthFirstSearch;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WordNet {

    private final List<String> synsets = new ArrayList<>();
    private final Map<String, Set<Integer>> map = new HashMap<>();
    private final SAP sap;

    // constructor takes the name of the two input files.
    // The constructor should take time linearithmic (or better) in the input size
    public WordNet(String synsetsName, String hypernymsName) throws IOException {
        Objects.requireNonNull(synsetsName);
        Objects.requireNonNull(hypernymsName);
        BufferedReader reader = new BufferedReader(new FileReader(synsetsName));
        while (reader.ready()) {
            String line = reader.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            synsets.add(id, parts[1]);
            for (String synonym : parts[1].split(" ")) {
                Set<Integer> vertices = map.get(synonym);
                if (vertices == null) {
                    vertices = new HashSet<>();
                }
                vertices.add(id);
                map.put(synonym, vertices);
            }
        }
        Digraph digraph = new Digraph(synsets.size());
        reader = new BufferedReader(new FileReader(hypernymsName));
        while (reader.ready()) {
            String line = reader.readLine();
            String[] parts = line.split(",");
            int v = Integer.parseInt(parts[0]);
            for (int wi = 1; wi < parts.length; ++wi) {
                int w = Integer.parseInt(parts[wi]);
                digraph.addEdge(v, w);
            }
        }
        verifyRootDAG(digraph);
        sap = new SAP(digraph);
    }

    private void verifyRootDAG(Digraph digraph) {
        final int size = digraph.V();
        Graph testGraph = new Graph(size);
        for (int vertex = 0; vertex < size; ++vertex) {
            for (int w : digraph.adj(vertex)) {
                testGraph.addEdge(vertex, w);
            }
        }
        DepthFirstSearch dfs = new DepthFirstSearch(testGraph, 0);
        if (dfs.count() != size) {
            throw new IllegalArgumentException("Not a rooted DAG.");
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    // in logarithmic time or better
    public boolean isNoun(String word) {
        Objects.requireNonNull(word);
        return map.get(word) != null;
    }

    private void verifyNoun(String word) {
        if (!isNoun(word)) {
            throw new IllegalArgumentException();
        }
    }

    // shortestDistance between nounA and nounB (defined below);
    // linear in the size of the WordNet digraph
    public int distance(String nounA, String nounB) {
        verifyNoun(nounA);
        verifyNoun(nounB);
        return sap.length(map.get(nounA), map.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below);
    // linear in the size of the WordNet digraph
    public String sap(String nounA, String nounB) {
        verifyNoun(nounA);
        verifyNoun(nounB);
        int ancestor = sap.ancestor(map.get(nounA), map.get(nounB));
        return synsets.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) throws IOException {
        WordNet wordNet = new WordNet("Lab6_WordNet/wordnet/mysyn.txt", "Lab6_WordNet/wordnet/myhyp.txt");
        boolean isNoun = wordNet.isNoun("O");
        isNoun = wordNet.isNoun("G");
        isNoun = wordNet.isNoun("D");
        int d = wordNet.distance("A", "H");
        d = wordNet.distance("B", "F");
        d = wordNet.distance("H", "E");
        String anc = wordNet.sap("E", "A");
        anc = wordNet.sap("B", "E");
        anc = wordNet.sap("B", "F");
    }
}
