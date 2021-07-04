/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class AcyclicSPTest {
    private DirectedEdgeTest[] edgeTo;
    private double[] distTo;

    public AcyclicSPTest(EdgeWeightedDigraphTest G, int s) {
        if (G == null)
            throw new IllegalArgumentException("argument to AcyclicSP() is null");

        edgeTo = new DirectedEdgeTest[G.V()];
        distTo = new double[G.V()];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        distTo[s] = 0;

        TopologicalTest topological = new TopologicalTest(G);
        if (!topological.hasOrder())
            throw new IllegalArgumentException("Digraph is not acyclic.");

        for (int v : topological.order()) {
            for (DirectedEdgeTest e : G.adj(v))
                relax(e);
        }
    }

    private void relax(DirectedEdgeTest e) {
        int v = e.from();
        int w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
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
        for (DirectedEdgeTest e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= distTo.length)
            throw new IllegalArgumentException("vertex is not valid");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraphTest G = new EdgeWeightedDigraphTest(in);

        // find shortest path from s to each other vertex in DAG
        AcyclicSPTest sp = new AcyclicSPTest(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (sp.hasPathTo(v)) {
                StdOut.printf("%d to %d (%.2f)  ", s, v, sp.distTo(v));
                for (DirectedEdgeTest e : sp.pathTo(v)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no path\n", s, v);
            }
        }
    }
}
