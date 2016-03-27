import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by dizcza on 27.03.16.
 */
public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        Objects.requireNonNull(nouns);
        if (nouns.length == 0) return null;
        int[] distance = new int[nouns.length];
        for (int i = 0; i < nouns.length; ++i) {
            for (int j = 0; j < nouns.length; ++j) {
                if (i != j) {
                    distance[i] += wordNet.distance(nouns[i], nouns[j]);
                }
            }
        }
        int maxDist = distance[0];
        String outc = nouns[0];
        for (int i = 1; i < distance.length; ++i) {
            if (distance[i] > maxDist) {
                maxDist = distance[i];
                outc = nouns[i];
            }
        }
        return outc;
    }

    // see test client below
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < args.length; ++i) {
            args[i] = "../testing/" + args[i];
        }
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
