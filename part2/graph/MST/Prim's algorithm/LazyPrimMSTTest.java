/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;

public class LazyPrimMSTTest {
    private static final double FLOATING_POINT_EPSILON = 1E-12;


    private double weight;
    private boolean[] marked;
    private Queue<EdgeTest> mst;
    private MinPQ<EdgeTest> pq;

    public LazyPrimMSTTest(EdgeWeightedGraphTest G) {
        if (G == null)
            throw new IllegalArgumentException("argument to LazyPrimMST is null");

        marked = new boolean[G.V()];
        pq = new MinPQ<>();
        mst = new Queue<>();

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v])
                prime(G, v);
        }
        assert check(G);
    }

    private void prime(EdgeWeightedGraphTest G, int s) {
        scan(G, s);

        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            EdgeTest e = pq.delMin();
            int v = e.either();
            int w = e.other(v);
            // Lazy Prime, so there will be some obsolete edges
            if (marked[v] && marked[w])
                continue;
            // add edge to MST
            mst.enqueue(e);
            weight += e.weight();

            if (!marked[v]) scan(G, v);
            if (!marked[w]) scan(G, w);
        }
    }

    public double weight() {
        return weight;
    }

    public Iterable<EdgeTest> edges() {
        return mst;
    }

    private void scan(EdgeWeightedGraphTest G, int v) {
        marked[v] = true;
        for (EdgeTest e : G.adj(v)) {
            if (!marked[e.other(v)]) {
                pq.insert(e);
            }
        }
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraphTest G) {

        // check weight
        double totalWeight = 0.0;
        for (EdgeTest e : edges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight,
                              weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(G.V());
        for (EdgeTest e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.find(v) == uf.find(w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (EdgeTest e : G.edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.find(v) != uf.find(w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (EdgeTest e : edges()) {

            // all edges in MST except e
            uf = new UF(G.V());
            for (EdgeTest f : mst) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (EdgeTest f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (uf.find(x) != uf.find(y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraphTest G = new EdgeWeightedGraphTest(in);
        LazyPrimMSTTest mst = new LazyPrimMSTTest(G);
        for (EdgeTest e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }
}
