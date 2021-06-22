/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class CycleTest {
    private boolean[] marked;
    private int[] edgeTo;
    private Stack<Integer> cycle;

    public CycleTest(GraphTest G) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        for (int v = 0; v < marked.length; v++) {
            if (!marked[v])
                dfs(G, v, -1);
        }
    }

    private void dfs(GraphTest G, int v, int u) {
        marked[v] = true;

        for (int w : G.adj(v)) {

            if (cycle != null) return;

            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w, v);
            }
            else if (w != u) {
                // 對acyclic graph來說, 遇到w已經visit過的話, 必定沿著同一個edge(v - w)來的, 所以 w 必定等於 u
                // 若發生 w != u 的情況, 就表是有cycle
                cycle = new Stack<>();
                // 從v開始往回找, 找到w停止(回追到上一個w即是cycle的起點)
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                // 再把w, v加入stack
                cycle.push(w);
                cycle.push(v);
            }
        }
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        GraphTest G = new GraphTest(in);
        CycleTest finder = new CycleTest(G);
        if (finder.hasCycle()) {
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
        else {
            StdOut.println("Graph is acyclic");
        }
    }
}
