/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class FlowNetworkTest {
    private final int V;
    private int E;
    private Bag<FlowEdgeTest>[] adj;

    public FlowNetworkTest(int V) {
        this.V = V;
        this.E = 0;
        this.adj = new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
    }

    public FlowNetworkTest(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            double capacity = StdRandom.uniform(100);
            addEdge(new FlowEdgeTest(v, w, capacity));
        }
    }

    public FlowNetworkTest(In in) {
        this(in.readInt());
        int numOfEdges = in.readInt();
        if (numOfEdges < 0)
            throw new IllegalArgumentException("number of edges must be non-negative");
        for (int i = 0; i < numOfEdges; i++) {
            int v = in.readInt();
            int w = in.readInt();
            validateVertex(v);
            validateVertex(w);
            double capacity = in.readDouble();
            addEdge(new FlowEdgeTest(v, w, capacity));
        }
    }

    public void addEdge(FlowEdgeTest e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public Iterable<FlowEdgeTest> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public Iterable<FlowEdgeTest> edges() {
        Bag<FlowEdgeTest> bag = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (FlowEdgeTest e : adj[v]) {
                if (v == e.from()) // 只印出e.from()的(只列出capacity的方向)
                    bag.add(e);
            }
        }
        return bag;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("V = " + V + ", E = " + E + "\n");
        for (int v = 0; v < V; v++) {
            sb.append(v + ":  ");
            for (FlowEdgeTest e : adj[v])
                if (v == e.from()) sb.append(e + "  "); // 只印出e.from()的(只列出capacity的方向)
            sb.append("\n");
        }
        return sb.toString();
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= adj.length)
            throw new IllegalArgumentException("vertex is not valid");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        FlowNetworkTest G = new FlowNetworkTest(in);
        StdOut.println(G);
    }
}
