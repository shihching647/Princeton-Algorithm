/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class EdgeWeightedDirectedCycleTest {
    private boolean[] marked;
    private DirectedEdgeTest[] edgeTo;
    private boolean[] onStack;
    private Stack<DirectedEdgeTest> cycle;

    public EdgeWeightedDirectedCycleTest(EdgeWeightedDigraphTest G) {
        if (G == null)
            throw new IllegalArgumentException("argument to EdgeWeightedDirectedCycle() is null");

        marked = new boolean[G.V()];
        edgeTo = new DirectedEdgeTest[G.V()];
        onStack = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v])
                dfs(G, v);
        }
    }

    private void dfs(EdgeWeightedDigraphTest G, int v) {
        marked[v] = true;
        onStack[v] = true;
        for (DirectedEdgeTest e : G.adj(v)) {
            if (cycle != null)
                return;

            int w = e.to();

            if (!marked[w]) {
                edgeTo[w] = e;
                dfs(G, w);
            }
            else if (onStack[w]) {
                cycle = new Stack<>();

                DirectedEdgeTest f = e;
                while (f.from() != w) {
                    cycle.push(f);
                    f = edgeTo[f.from()];
                }
                cycle.push(f);
                return;
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<DirectedEdgeTest> cycle() {
        return cycle;
    }

    public static void main(String[] args) {

        // create random DAG with V vertices and E edges; then add F random edges
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);
        EdgeWeightedDigraphTest G = new EdgeWeightedDigraphTest(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < E; i++) {
            int v, w;
            do {
                v = StdRandom.uniform(V);
                w = StdRandom.uniform(V);
            } while (v >= w);
            double weight = StdRandom.uniform();
            G.addEdge(new DirectedEdgeTest(v, w, weight));
        }

        // add F extra edges
        for (int i = 0; i < F; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            double weight = StdRandom.uniform(0.0, 1.0);
            G.addEdge(new DirectedEdgeTest(v, w, weight));
        }

        StdOut.println(G);

        // find a directed cycle
        EdgeWeightedDirectedCycleTest finder = new EdgeWeightedDirectedCycleTest(G);
        if (finder.hasCycle()) {
            StdOut.print("Cycle: ");
            for (DirectedEdgeTest e : finder.cycle()) {
                StdOut.print(e + " ");
            }
            StdOut.println();
        }

        // or give topologial sort
        else {
            StdOut.println("No directed cycle");
        }
    }
}
