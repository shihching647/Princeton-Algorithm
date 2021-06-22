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

public class BreadthFirstPathsTest {
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;

    public BreadthFirstPathsTest(GraphTest G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        Arrays.fill(distTo, Integer.MAX_VALUE); // init distTo to Integer.MAX_VALUE
        validateVertex(s);
        bfs(G, s);
    }

    public BreadthFirstPathsTest(GraphTest G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        Arrays.fill(distTo, Integer.MAX_VALUE); // init distTo to Integer.MAX_VALUE
        validateVertices(sources);
        bfs(G, sources);
    }

    private void bfs(GraphTest G, int s) {
        Queue<Integer> q = new Queue<>();
        q.enqueue(s);
        distTo[s] = 0;
        marked[s] = true;

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    q.enqueue(w);
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                }
            }
        }
    }

    private void bfs(GraphTest G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<>();
        for (int s : sources) {
            q.enqueue(s);
            distTo[s] = 0;
            marked[s] = true;
        }

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    q.enqueue(w);
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public int distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v))
            return null;

        Stack<Integer> stack = new Stack<>();
        while (distTo(v) != 0) {
            stack.push(v);
            v = edgeTo[v];
        }
        stack.push(v);
        return stack;
    }

    private void validateVertex(int v) {
        if (v < 0 || v > marked.length)
            throw new IllegalArgumentException("Vertex is not valid");
    }

    // throw an IllegalArgumentException if vertices is null, has zero vertices,
    // or has a vertex not between 0 and V-1
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = marked.length;
        int count = 0;
        for (Integer v : vertices) {
            count++;
            if (v == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            validateVertex(v);
        }
        if (count == 0) {
            throw new IllegalArgumentException("zero vertices");
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        GraphTest G = new GraphTest(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        // List<Integer> sources = new ArrayList<Integer>(Arrays.asList(0, 3));
        BreadthFirstPathsTest bfs = new BreadthFirstPathsTest(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d (-):  not connected\n", s, v);
            }

        }
    }
}
