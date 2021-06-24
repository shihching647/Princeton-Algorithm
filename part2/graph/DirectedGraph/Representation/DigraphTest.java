/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class DigraphTest {
    private int E, V;
    private Bag<Integer>[] adj;
    private int[] indegree;

    public DigraphTest(int V) {
        if (V <= 0)
            throw new IllegalArgumentException("V can not be an negative number");
        this.V = V;
        this.indegree = new int[V];
        adj = new Bag[V];
        for (int i = 0; i < V; i++)
            adj[i] = new Bag<Integer>();
    }

    public DigraphTest(In in) {
        try {
            V = in.readInt();
            if (V <= 0)
                throw new IllegalArgumentException("V can not be an negative number");

            indegree = new int[V];
            adj = new Bag[V];
            int numOfEdges = in.readInt();
            if (numOfEdges < 0)
                throw new IllegalArgumentException("E can not be an negative number");

            // init adj
            for (int i = 0; i < V; i++) {
                adj[i] = new Bag<Integer>();
            }

            for (int i = 0; i < numOfEdges; i++) {
                int v = in.readInt();
                int w = in.readInt();
                addEdge(v, w);
            }

        }
        catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("invalid input format in Digraph constructor", ex);
        }
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        indegree[w]++;
        E++;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public DigraphTest reverse() {
        DigraphTest reverse = new DigraphTest(V);
        for (int v = 0; v < V; v++) {
            for (int w : adj[v]) {
                reverse.addEdge(w, v);
            }
        }
        return reverse;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex is not legal");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V + " vertices, " + E + " edges\n");
        for (int v = 0; v < V; v++) {
            sb.append(v + ": ");
            for (int w : adj[v]) {
                sb.append(w + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        DigraphTest G = new DigraphTest(in);
        StdOut.println(G);
        System.out.println(G.reverse());

        StdOut.println(G.outdegree(6));
        System.out.println(G.indegree(0));
        for (int w : G.adj(9)) {
            System.out.println(w + ", ");
        }
    }
}
