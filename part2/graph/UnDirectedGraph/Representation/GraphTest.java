/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class GraphTest {

    private Bag<Integer>[] adj;
    private int V;
    private int E;

    public GraphTest(In in) {
        if (in == null)
            throw new IllegalArgumentException("argument is null");

        try {
            V = in.readInt();

            // init adjacency list
            adj = (Bag<Integer>[]) new Bag[V];
            for (int i = 0; i < V; i++) {
                adj[i] = new Bag<Integer>();
            }

            int numOfEdge = in.readInt();
            // add edges
            for (int i = 0; i < numOfEdge; i++) {
                int v = in.readInt();
                int w = in.readInt();
                addEdge(v, w);
            }
        }
        catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", ex);
        }
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("V = " + V + ", E = " + E + "\n");
        for (int i = 0; i < V; i++) {
            sb.append(i + ": ");
            for (int e : adj[i]) {
                sb.append(e + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex is invalid");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        GraphTest G = new GraphTest(in);
        StdOut.println(G);
        System.out.println(G.degree(0));
    }
}
