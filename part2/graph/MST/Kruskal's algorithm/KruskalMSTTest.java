/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;

import java.util.Arrays;

public class KruskalMSTTest {
    private Queue<EdgeTest> mst;
    private double weight;

    public KruskalMSTTest(EdgeWeightedGraphTest G) {
        if (G == null)
            throw new IllegalArgumentException("argument to KruskalMST is null");

        EdgeTest[] edges = new EdgeTest[G.E()];
        int t = 0;
        for (EdgeTest e : G.edges()) {
            edges[t++] = e;
        }
        Arrays.sort(edges);

        mst = new Queue<>();
        UF uf = new UF(G.V());

        for (int i = 0; i < edges.length && mst.size() < G.V() - 1; i++) {
            EdgeTest e = edges[i];
            int v = e.either();
            int w = e.other(v);
            if (uf.find(v) != uf.find(w)) {
                mst.enqueue(e);
                uf.union(v, w);
                weight += e.weight();
            }
        }
    }

    public double weight() {
        return weight;
    }

    public Iterable<EdgeTest> edges() {
        return mst;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraphTest G = new EdgeWeightedGraphTest(in);
        KruskalMSTTest mst = new KruskalMSTTest(G);
        for (EdgeTest e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }
}
