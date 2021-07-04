/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class EdgeWeightedDigraphTest {
    private final int V;
    private int E;
    private Bag<DirectedEdgeTest>[] adj;
    private int[] indegree;

    public EdgeWeightedDigraphTest(In in) {
        if (in == null)
            throw new IllegalArgumentException("argument to EdgeWeightedDigraph is null");

        try {
            V = in.readInt();
            indegree = new int[V];
            adj = new Bag[V];
            for (int v = 0; v < V; v++)
                adj[v] = new Bag<>();

            int numOfEdges = in.readInt();
            for (int i = 0; i < numOfEdges; i++) {
                int v = in.readInt();
                int w = in.readInt();
                double weight = in.readDouble();
                addEdge(new DirectedEdgeTest(v, w, weight));
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException(
                    "invalid input format in EdgeWeightedDigraph constructor", e);
        }
    }

    // deeply copy
    public EdgeWeightedDigraphTest(EdgeWeightedDigraphTest G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < V; v++) {
            // copy indegree[]
            indegree[v] = G.indegree(v);

            // copy adj[v], keep the order same with original digraph
            Stack<DirectedEdgeTest> stack = new Stack<>();
            for (DirectedEdgeTest e : G.adj(v))
                stack.push(e);
            for (DirectedEdgeTest e : stack)
                adj[v].add(e);
        }
    }

    public EdgeWeightedDigraphTest(int V) {
        if (V < 0)
            throw new IllegalArgumentException();
        this.V = V;
        indegree = new int[V];
        adj = new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(DirectedEdgeTest e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }

    public Iterable<DirectedEdgeTest> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public Iterable<DirectedEdgeTest> edges() {
        Bag<DirectedEdgeTest> list = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdgeTest e : adj[v])
                list.add(e);
        }
        return list;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex is not valid");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("V = " + V + " E = " + E + "\n");
        for (int v = 0; v < V; v++) {
            sb.append(v + ": ");
            for (DirectedEdgeTest e : adj[v]) {
                sb.append(e + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraphTest G = new EdgeWeightedDigraphTest(in);
        StdOut.println(G);
    }
}
