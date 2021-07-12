/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class FlowEdgeTest {
    // to deal with floating-point roundoff errors
    private static final double FLOATING_POINT_EPSILON = 1E-10;

    private final int v;
    private final int w;
    private final double capacity;
    private double flow;

    public FlowEdgeTest(int v, int w, double capacity) {
        if (v < 0 || w < 0)
            throw new IllegalArgumentException("vertex is not valid");
        if (capacity < 0.0)
            throw new IllegalArgumentException("Edge capacity must be non-negative");
        this.v = v;
        this.w = w;
        this.capacity = capacity;
    }

    public FlowEdgeTest(int v, int w, double capacity, double flow) {
        if (v < 0 || w < 0)
            throw new IllegalArgumentException("vertex is not valid");
        if (capacity < 0.0)
            throw new IllegalArgumentException("Edge capacity must be non-negative");
        if (flow > capacity)
            throw new IllegalArgumentException("flow exceeds capacity");
        if (flow < 0.0)
            throw new IllegalArgumentException("flow must be non-negative");
        this.v = v;
        this.w = w;
        this.capacity = capacity;
        this.flow = flow;
    }

    public FlowEdgeTest(FlowEdgeTest e) {
        this.v = e.v;
        this.w = e.w;
        this.capacity = e.capacity;
        this.flow = e.flow;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public double capacity() {
        return capacity;
    }

    public double flow() {
        return flow;
    }

    public int other(int vertex) {
        if (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("invalid endpoint");
    }

    public double residualCapacityTo(int vertex) {
        if (vertex == v) return flow;                   // backward edge
        else if (vertex == w) return capacity - flow;   // forward edge
        else throw new IllegalArgumentException("invalid endpoint");
    }

    public void addResidualFlowTo(int v, double delta) {
        if (delta < 0.0)
            throw new IllegalArgumentException("Delta must be non-negative");

        if (v == this.v) flow -= delta;
        else if (v == this.w) flow += delta;

        // round flow to 0 or capacity if within floating-point precision
        if (Math.abs(flow) <= FLOATING_POINT_EPSILON)
            flow = 0;
        if (Math.abs(flow - capacity) <= FLOATING_POINT_EPSILON)
            flow = capacity;

        // check flow valid
        if (flow > capacity)
            throw new IllegalArgumentException("flow exceeds capacity");
        if (flow < 0.0)
            throw new IllegalArgumentException("flow must be non-negative");
    }

    public String toString() {
        return v + "->" + w + " " + flow + "/" + capacity;
    }

    public static void main(String[] args) {
        FlowEdgeTest e = new FlowEdgeTest(12, 23, 4.56);
        StdOut.println(e);
    }
}
