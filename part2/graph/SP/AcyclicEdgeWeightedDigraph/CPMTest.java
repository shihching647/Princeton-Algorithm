/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class CPMTest {

    private CPMTest() {
    }

    public static void main(String[] args) {
        // n個jobs, 每個job有兩個vertex(start, end)
        // job1: 0為start, n為end
        // job2: 1為start, 1 + n為end
        // 故0 ~ 2n - 1為job的vertex
        int n = StdIn.readInt();

        // 2n, 2n + 1分別為source, sink
        int source = 2 * n;
        int sink = 2 * n + 1;

        // 0 ~ 2n + 1 共2n + 2個vertex
        EdgeWeightedDigraphTest G = new EdgeWeightedDigraphTest(2 * n + 2);
        for (int i = 0; i < n; i++) {
            int start = i, end = i + n;
            double duration = StdIn.readDouble();
            G.addEdge(new DirectedEdgeTest(source, start, 0)); // source to begin (0 weight)
            G.addEdge(new DirectedEdgeTest(end, sink, 0)); // end to sink (0 weight)
            G.addEdge(new DirectedEdgeTest(start, end,
                                           duration)); // begin to end (weighted by duration)

            // precedence constraints
            int m = StdIn.readInt();
            for (int j = 0; j < m; j++) {
                int postJob = StdIn.readInt();
                G.addEdge(new DirectedEdgeTest(end, postJob, 0));
            }
        }

        // compute longest path
        AcyclicLPTest lp = new AcyclicLPTest(G, source);

        // print results
        StdOut.println(" job   start  finish");
        StdOut.println("--------------------");
        for (int i = 0; i < n; i++) {
            StdOut.printf("%4d %7.1f %7.1f\n", i, lp.distTo(i), lp.distTo(i + n));
        }
        StdOut.printf("Finish time: %7.1f\n", lp.distTo(sink));
    }
}
