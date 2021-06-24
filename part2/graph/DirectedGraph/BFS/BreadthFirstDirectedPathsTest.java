/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BreadthFirstDirectedPathsTest {
    private boolean[] marked;
    private int[] distTo;
    private int[] edgeTo;

    public BreadthFirstDirectedPathsTest(DigraphTest G, int s) {
        marked = new boolean[G.V()];
        validateVertex(s);
        distTo = new int[G.V()];
        Arrays.fill(distTo, Integer.MAX_VALUE);
        edgeTo = new int[G.V()];
        bfs(G, s);
    }

    private void bfs(DigraphTest G, int s) {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(s);
        marked[s] = true;
        distTo[s] = 0;

        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    queue.enqueue(w);
                }
            }
        }
    }

    public int distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<Integer> stack = new Stack<>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            stack.push(x);
        stack.push(x);
        return stack;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= marked.length)
            throw new IllegalArgumentException("vertex is not valid");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        DigraphTest G = new DigraphTest(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        BreadthFirstDirectedPathsTest bfs = new BreadthFirstDirectedPathsTest(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else StdOut.print("->" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d (-):  not connected\n", s, v);
            }

        }
    }
}
