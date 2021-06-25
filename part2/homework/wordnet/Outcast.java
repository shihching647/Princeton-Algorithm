/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null)
            throw new IllegalArgumentException("argument for Outcast() is null");
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        // Assume that argument to outcast() contains only valid wordnet nouns

        if (nouns == null)
            throw new IllegalArgumentException("argument for outcast() is null");
        for (String noun : nouns) {
            if (noun == null)
                throw new IllegalArgumentException("argument for outcast() nouns contains null");
        }
        String outcast = nouns[0];
        int maxLength = 0;
        for (int i = 0; i < nouns.length; i++) {
            int totalLength = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i != j) {
                    totalLength += wordNet.distance(nouns[i], nouns[j]);
                }
            }
            if (totalLength > maxLength) {
                maxLength = totalLength;
                outcast = nouns[i];
            }
        }
        return outcast;
    }


    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
