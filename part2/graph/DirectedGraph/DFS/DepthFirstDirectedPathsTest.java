/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstDirectedPathsTest {
    private boolean[] marked;
    private int[] edgeTo;
    private final int s;

    public DepthFirstDirectedPathsTest(DigraphTest G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        validateVertex(s);
        this.s = s;
        dfs(G, s);
    }

    private void dfs(DigraphTest G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<Integer> stack = new Stack<>();
        for (int x = v; x != s; x = edgeTo[x])
            stack.push(x);
        stack.push(s);
        return stack;
    }


    private void validateVertex(int v) {
        if (v < 0 || v >= marked.length)
            throw new IllegalArgumentException("edge is not valid");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        DigraphTest G = new DigraphTest(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        DepthFirstDirectedPathsTest dfs = new DepthFirstDirectedPathsTest(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (dfs.hasPathTo(v)) {
                StdOut.printf("%d to %d:  ", s, v);
                for (int x : dfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d:  not connected\n", s, v);
            }

        }
    }
}
