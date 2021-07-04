/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class DijkstraSPTest {
    private DirectedEdgeTest[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public DijkstraSPTest(EdgeWeightedDigraphTest G, int s) {
        if (G == null)
            throw new IllegalArgumentException("argument to DijkstraSP() is null");

        // check all edges are non-negative
        for (DirectedEdgeTest e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("Digraph contains negative edge");
        }

        edgeTo = new DirectedEdgeTest[G.V()];
        distTo = new double[G.V()];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY); // init distTo[] to infinite
        pq = new IndexMinPQ<>(G.V());

        distTo[s] = 0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdgeTest e : G.adj(v))
                relax(e);
        }

        // check optimality conditions
        assert check(G, s);
    }

    private void relax(DirectedEdgeTest e) {
        int v = e.from();
        int w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else pq.insert(w, distTo[w]);
        }
    }

    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdgeTest> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdgeTest> path = new Stack<>();
        for (int x = v; edgeTo[x] != null; x = edgeTo[x].from())
            path.push(edgeTo[x]);
        return path;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= distTo.length)
            throw new IllegalArgumentException("vertex is not valid");
    }

    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(EdgeWeightedDigraphTest G, int s) {

        // check edge weights are non-negative
        for (DirectedEdgeTest e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (DirectedEdgeTest e : G.edges()) {
            int v = e.from();
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                System.err.println("edge " + e + " not relaxed");
                return false;
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdgeTest e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraphTest G = new EdgeWeightedDigraphTest(in);
        int s = Integer.parseInt(args[1]);

        // compute shortest paths
        DijkstraSPTest sp = new DijkstraSPTest(G, s);


        // print shortest path
        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(t)) {
                StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
                for (DirectedEdgeTest e : sp.pathTo(t)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no path\n", s, t);
            }
        }
    }
}
