import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

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

    private final static byte BLUE = 1;
    private final static byte CYAN = 2;
    private final static byte MIXED = BLUE | CYAN;

    private final List<String> synsets = new ArrayList<>();
    private final Map<String, Set<Integer>> map = new HashMap<>();
    private final Digraph digraph;
    private final int V;

    private class SAP {
        private int shortestDistance = Integer.MAX_VALUE;
        private String ancestor;

        private boolean opposite(final byte c1, final byte c2) {
            return (c1 | c2) == MIXED;
        }

        private void verifyNoun(String noun) {
            if (!isNoun(noun)) {
                throw new IllegalArgumentException();
            }
        }

        private SAP(String nounA, String nounB) {
            verifyNoun(nounA);
            verifyNoun(nounB);
            if (nounA.equals(nounB)) {
                ancestor = nounA;
                shortestDistance = 0;
                return;
            }
            byte[] color = new byte[V];
            int[] distance = new int[V];
            for (int vertex = 0; vertex < V; ++vertex) {
                distance[vertex] = Integer.MAX_VALUE;
            }
            Queue<Integer> queue = new Queue<>();
            for (int va : map.get(nounA)) {
                queue.enqueue(va);
                color[va] = CYAN;
                distance[va] = 0;
            }
            for (int vb : map.get(nounB)) {
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
                    ancestor = synsets.get(vertex);
                }
            }
            if (shortestDistance == Integer.MAX_VALUE) {
                shortestDistance = -1;
            }
        }
    }

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
        V = synsets.size();
        digraph = new Digraph(V);
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

    // shortestDistance between nounA and nounB (defined below);
    // linear in the size of the WordNet digraph
    public int distance(String nounA, String nounB) {
        return new SAP(nounA, nounB).shortestDistance;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below);
    // linear in the size of the WordNet digraph
    public String sap(String nounA, String nounB) {
        return new SAP(nounA, nounB).ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) throws IOException {
        WordNet wordNet = new WordNet("Lab6_WordNet/wordnet/mysyn.txt", "Lab6_WordNet/wordnet/myhyp.txt");
        boolean isNoun = wordNet.isNoun("I");
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
