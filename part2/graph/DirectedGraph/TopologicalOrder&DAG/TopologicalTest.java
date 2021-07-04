/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class TopologicalTest {
    private int[] rank;
    private Iterable<Integer> order;

    public TopologicalTest(DigraphTest G) {
        DirectedCycleTest finder = new DirectedCycleTest(G);
        if (!finder.hasCycle()) {
            DepthFirstOrderTest dfs = new DepthFirstOrderTest(G);
            order = dfs.reversePost();
            rank = new int[G.V()];
            int count = 0;
            for (int v : order) {
                rank[v] = count++;
            }
        }
    }

    public TopologicalTest(EdgeWeightedDigraphTest G) {
        EdgeWeightedDirectedCycleTest finder = new EdgeWeightedDirectedCycleTest(G);
        if (!finder.hasCycle()) {
            DepthFirstOrderTest dfs = new DepthFirstOrderTest(G);
            order = dfs.reversePost();
            rank = new int[G.V()];
            int count = 0;
            for (int v : order)
                rank[v] = count++;
        }
    }

    public boolean hasOrder() {
        return order != null;
    }

    public Iterable<Integer> order() {
        return order;
    }

    public int rank(int v) {
        validateVertex(v);
        if (hasOrder()) return rank[v];
        else return -1;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= rank.length)
            throw new IllegalArgumentException("vertex is bot valid");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        DigraphTest G = new DigraphTest(in);

        TopologicalTest topological = new TopologicalTest(G);
        for (int v : topological.order()) {
            StdOut.println(v);
        }
    }
}
