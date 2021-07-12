/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  647
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class FordFulkersonTest {
    private static final double FLOATING_POINT_EPSILON = 1E-11;

    private final int V;
    private boolean[] marked;
    private FlowEdgeTest[] edgeTo;
    private double value;

    public FordFulkersonTest(FlowNetworkTest G, int s, int t) {
        V = G.V();
        validateVertex(s);
        validateVertex(t);
        if (s == t) throw new IllegalArgumentException("Source equals sink");
        if (!isFeasible(G, s, t)) throw new IllegalArgumentException("Initial flow is infeasible");

        value = excess(G, s); // 不能只設為0, 因為原來的flow network可能就含有流量
        while (hasAugmentingPath(G, s, t)) {
            // find bottleneck capacity
            double bottleneck = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v))
                bottleneck = Math.min(bottleneck, edgeTo[v].residualCapacityTo(v));

            // augment flow
            for (int v = t; v != s; v = edgeTo[v].other(v))
                edgeTo[v].addResidualFlowTo(v, bottleneck);

            value += bottleneck;
        }
    }

    // is there an augmenting path?
    // if so, upon termination edgeTo[] will contain a parent-link representation of such a path
    // this implementation finds a shortest augmenting path (fewest number of edges),
    // which performs well both in theory and in practice
    private boolean hasAugmentingPath(FlowNetworkTest G, int s, int t) {
        marked = new boolean[V];
        edgeTo = new FlowEdgeTest[V];
        Queue<Integer> q = new Queue<>();
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty() && !marked[t]) {
            int v = q.dequeue();
            for (FlowEdgeTest e : G.adj(v)) {
                int w = e.other(v);
                if (!marked[w] && e.residualCapacityTo(w) > FLOATING_POINT_EPSILON) {
                    marked[w] = true;
                    edgeTo[w] = e;
                    q.enqueue(w);
                }
            }
        }

        // is there an augmenting path?
        return marked[t];
    }

    // 取得v點的淨流量
    private double excess(FlowNetworkTest G, int v) {
        double excess = 0.0;
        for (FlowEdgeTest e : G.adj(v)) {
            if (v == e.from()) excess -= e.flow(); // 流出去
            else excess += e.flow();               // 流進來
        }
        return excess;
    }

    public double value() {
        return value;
    }

    public boolean inCut(int v) {
        validateVertex(v);
        return marked[v]; // 因為最後一次找不到augmenting path時, 能夠visit都是跟s在同一個cut裡面
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex is not valid");
    }

    private boolean isFeasible(FlowNetworkTest G, int s, int t) {

        // check that capacity constraints are satisfied
        // flow介於 0 ~ e.capacity()之間(這裡使用FLOATING_POINT_EPSILON避免進位問題)
        for (int v = 0; v < V; v++) {
            for (FlowEdgeTest e : G.adj(v)) {
                if (e.flow() < -FLOATING_POINT_EPSILON
                        || e.flow() > e.capacity() + FLOATING_POINT_EPSILON) {
                    System.out.println("Edge does not satisfy capacity constraints: " + e);
                    return false;
                }
            }
        }

        // check that net flow into a vertex equals zero, except at source and sink
        // 總flow + s的淨流量會等於0
        if (Math.abs(value + excess(G, s)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at source = " + excess(G, s));
            System.err.println("Max flow         = " + value);
            return false;
        }
        // 總flow - t的淨流量會等於0
        if (Math.abs(value - excess(G, t)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at sink   = " + excess(G, t));
            System.err.println("Max flow         = " + value);
            return false;
        }
        // 除了s和t, 其他點v的淨流量要等於0
        for (int v = 0; v < V; v++) {
            if (v != s && v != t) {
                if (Math.abs(excess(G, v)) > FLOATING_POINT_EPSILON) {
                    System.err.println("Net flow out of " + v + " doesn't equal zero");
                    return false;
                }
            }
        }
        return true;
    }

    // check optimality conditions
    private boolean check(FlowNetworkTest G, int s, int t) {

        // check that flow is feasible
        if (!isFeasible(G, s, t)) {
            System.err.println("Flow is infeasible");
            return false;
        }

        // check s is in the cut
        if (!inCut(s)) {
            System.err.println("source " + s + " is not on source side of min cut");
            return false;
        }
        // check t is not in the cut
        if (inCut(t)) {
            System.err.println("sink " + t + " is on source side of min cut");
            return false;
        }

        // check that value of min cut = value of max flow
        double mincutValue = 0.0;
        for (int v = 0; v < V; v++) {
            for (FlowEdgeTest e : G.adj(v)) {
                // 只看capacity, 所以v == e.from(), 且v在cut裡, w不在cut裡
                if (v == e.from() && inCut(v) && !inCut(e.to()))
                    mincutValue += e.capacity();
            }
        }
        if (Math.abs(mincutValue - value) > FLOATING_POINT_EPSILON) {
            System.err.println("min cut value != net value");
            System.err.println("Max flow value = " + value + ", min cut value = " + mincutValue);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        int t = Integer.parseInt(args[2]);
        FlowNetworkTest G = new FlowNetworkTest(in);
        StdOut.println(G);

        // compute maximum flow and minimum cut
        FordFulkersonTest maxflow = new FordFulkersonTest(G, s, t);
        StdOut.println("Max flow from " + s + " to " + t);
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdgeTest e : G.adj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    StdOut.println("   " + e);
            }
        }

        // print min-cut
        StdOut.print("Min cut: ");
        for (int v = 0; v < G.V(); v++) {
            if (maxflow.inCut(v)) StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.println("Max flow value = " + maxflow.value());
    }
}
