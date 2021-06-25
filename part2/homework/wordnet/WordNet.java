/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private HashMap<String, Bag<Integer>> map; // String -> Iterable<index> (同一個詞可能出現好幾次)
    private ArrayList<String> keys;  // index -> Iterable<String>
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("arguments for WordNet() is null");

        map = new HashMap<>();
        keys = new ArrayList<>();
        In in = new In(synsets);
        int index = 0;

        while (!in.isEmpty()) {
            String[] token = in.readLine().split(",");
            index = Integer.parseInt(token[0]);
            String synset = token[1];
            for (String s : synset.split(" ")) {
                Bag<Integer> bag = map.getOrDefault(s, new Bag<>());
                bag.add(index);
                map.put(s, bag);
            }
            keys.add(synset);
        }

        Digraph G = new Digraph(index + 1);

        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] token = in.readLine().split(",");
            index = Integer.parseInt(token[0]);
            for (int i = 1; i < token.length; i++) {
                G.addEdge(index, Integer.parseInt(token[i]));
            }
        }

        // check single root
        int root = -1;
        for (int v = 0; v < G.V(); v++) {
            if (G.indegree(v) > 0 && G.outdegree(v) == 0) {
                if (root == -1) root = v;
                else throw new IllegalArgumentException("Digraph has more than two roots");
            }

        }
        // is it a DAG?
        DirectedCycle finder = new DirectedCycle(G);
        if (finder.hasCycle())
            throw new IllegalArgumentException("Digraph has cycle");

        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("argument for isNoun is null");
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        return sap.length(map.get(nounA), map.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        int ancestor = sap.ancestor(map.get(nounA), map.get(nounB));
        return keys.get(ancestor);
    }

    private void validateNoun(String noun) {
        if (noun == null)
            throw new IllegalArgumentException("argument can not be null");
        if (!isNoun(noun))
            throw new IllegalArgumentException(noun + " is not in WordNet");
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        System.out.println(wordnet.sap("worm", "bird"));
        System.out.println(wordnet.distance("white_marlin", "mileage")); // 23
        System.out.println(wordnet.distance("Black_Plague", "black_marlin")); // 33
        System.out.println(wordnet.distance("American_water_spaniel", "histology")); // 27
        System.out.println(wordnet.distance("Brown_Swiss", "barrel_roll")); // 29
    }
}
