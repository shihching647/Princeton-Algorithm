/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Eager version Prim's algorithm:
 * 與lazy version相比, PQ裡面存的是Vertex,
 * 每次新增新的edge到MST時, 都會update PQ裡面Vertex的weight
 * 故不會像lazy一樣存了很多用不到的edge
 * <p>
 * <p>
 * Time complexity: O(ElogV) (lazy : O(ElogE)
 * Space complexity: O(V) (lazy : O(E))
 */

public class PrimMSTTest {
    private boolean[] marked;
    private EdgeTest[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public PrimMSTTest(EdgeWeightedGraphTest G) {
        if (G == null)
            throw new IllegalArgumentException("argument for PrimMST is null");

        marked = new boolean[G.V()];
        edgeTo = new EdgeTest[G.V()];
        distTo = new double[G.V()];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        pq = new IndexMinPQ<>(G.V());

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v])
                prim(G, v);
        }
    }

    private void prim(EdgeWeightedGraphTest G, int s) {
        distTo[s] = 0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    private void scan(EdgeWeightedGraphTest G, int v) {
        marked[v] = true;
        for (EdgeTest e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue; // 如果另一端已經在MST內, continue
            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;

                // update PQ
                if (pq.contains(w)) pq.decreaseKey(w, e.weight());
                else pq.insert(w, e.weight());
            }
        }
    }

    public double weight() {
        double weight = 0;
        for (EdgeTest e : edges()) {
            weight += e.weight();
        }
        return weight;
    }

    public Iterable<EdgeTest> edges() {
        Queue<EdgeTest> mst = new Queue<>();
        for (int v = 0; v < edgeTo.length; v++) {
            EdgeTest e = edgeTo[v];
            if (e != null)
                mst.enqueue(e);
        }
        return mst;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraphTest G = new EdgeWeightedGraphTest(in);
        PrimMSTTest mst = new PrimMSTTest(G);
        for (EdgeTest e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }
}
