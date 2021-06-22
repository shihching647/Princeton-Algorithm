/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

/**
 * A graph G is bipartite if and only if it has no odd cycles.
 * bipartite -> no odd cycle (pf: https://www.youtube.com/watch?v=xQcCXSFVSks&ab_channel=WrathofMath)
 * no odd cycle -> bipartite (pf: https://www.youtube.com/watch?v=_TIqhvDR8DQ&ab_channel=WrathofMath)
 */

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.GraphGenerator;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class BipartiteTest {
    private boolean[] marked;
    private int[] edgeTo;
    private boolean[] color;
    private Stack<Integer> cycle;
    private boolean isBipartite;

    public BipartiteTest(Graph G) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        color = new boolean[G.V()];
        isBipartite = true;

        for (int v = 0; v < marked.length; v++) {
            if (!marked[v])
                dfs(G, v);
        }
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {

            if (cycle != null) return;

            if (!marked[w]) {
                edgeTo[w] = v;
                color[w] = !color[v];
                dfs(G, w);
            }
            else if (color[w] == color[v]) {
                isBipartite = false;
                cycle = new Stack<>();
                cycle.push(w);
                for (int x = v; x != w; x = edgeTo[x])
                    cycle.push(x);
                cycle.push(w);
            }
        }
    }

    public boolean isBipartite() {
        return isBipartite;
    }

    public boolean color(int v) {
        validateVertex(v);
        if (isBipartite)
            return color[v];
        throw new UnsupportedOperationException("graph is not bipartite");
    }

    // if isBipartite -> return null
    // else return odd cycle
    public Iterable<Integer> oddCycle() {
        return cycle;
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    public static void main(String[] args) {
        int V1 = Integer.parseInt(args[0]);
        int V2 = Integer.parseInt(args[1]);
        int E = Integer.parseInt(args[2]);
        int F = Integer.parseInt(args[3]);

        // create random bipartite graph with V1 vertices on left side,
        // V2 vertices on right side, and E edges; then add F random edges
        Graph G = GraphGenerator.bipartite(V1, V2, E);
        for (int i = 0; i < F; i++) {
            int v = StdRandom.uniform(V1 + V2);
            int w = StdRandom.uniform(V1 + V2);
            G.addEdge(v, w);
        }

        StdOut.println(G);


        BipartiteTest b = new BipartiteTest(G);
        if (b.isBipartite()) {
            StdOut.println("Graph is bipartite");
            for (int v = 0; v < G.V(); v++) {
                StdOut.println(v + ": " + b.color(v));
            }
        }
        else {
            StdOut.print("Graph has an odd-length cycle: ");
            for (int x : b.oddCycle()) {
                StdOut.print(x + " ");
            }
            StdOut.println();
        }
    }
}
