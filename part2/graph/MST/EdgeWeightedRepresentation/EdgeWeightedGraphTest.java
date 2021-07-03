/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class EdgeWeightedGraphTest {
    private final int V;
    private int E;
    private Bag<EdgeTest>[] adj;

    public EdgeWeightedGraphTest(In in) {
        if (in == null)
            throw new IllegalArgumentException("argument is null");

        try {
            V = in.readInt();
            adj = new Bag[V];
            for (int i = 0; i < V; i++) {
                adj[i] = new Bag<>();
            }

            int numOfEdges = in.readInt();
            for (int i = 0; i < numOfEdges; i++) {
                int v = in.readInt();
                int w = in.readInt();
                double weight = in.readDouble();
                addEdge(new EdgeTest(v, w, weight));
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException(
                    "invalid input format in EdgeWeightedGraph constructor", e);
        }


    }

    public void addEdge(EdgeTest e) {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Iterable<EdgeTest> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int E() {
        return E;
    }

    public int V() {
        return V;
    }

    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public Iterable<EdgeTest> edges() {
        Bag<EdgeTest> list = new Bag<>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (EdgeTest e : adj[v]) {
                if (v > e.other(v)) {
                    list.add(e);
                }
                // add only one copy of each self loop (self loops will be consecutive)
                // 因為 addEdge() 裡面, v == w 時候, 會在adj[v] 連續加兩次
                else if (v == e.other(v)) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("V = " + V + ", E = " + E + "\n");
        for (int i = 0; i < V; i++) {
            sb.append(i + ": ");
            for (EdgeTest e : adj[i]) {
                sb.append(e.toString() + " ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex is not valid");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraphTest G = new EdgeWeightedGraphTest(in);
        StdOut.println(G);
        System.out.println(G.degree(7));
        for (EdgeTest e : G.edges()) {
            System.out.println(e);
        }
    }
}
