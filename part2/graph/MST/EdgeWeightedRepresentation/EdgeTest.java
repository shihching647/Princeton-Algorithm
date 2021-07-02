/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class EdgeTest {
    private final int v;
    private final int w;
    private final double weight;

    public EdgeTest(int v, int w, double weight) {
        if (v < 0 || w < 0)
            throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (Double.isNaN(weight))
            throw new IllegalArgumentException("weight must is NAN");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if (vertex == v) return this.w;
        else if (vertex == w) return this.v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    public int compareTo(EdgeTest that) {
        return Double.compare(this.weight, that.weight);
    }

    public String toString() {
        return String.format("%d-%d %.5f", v, w, weight);
    }

    public static void main(String[] args) {
        Edge e = new Edge(12, 34, 5.67);
        StdOut.println(e);
    }
}
