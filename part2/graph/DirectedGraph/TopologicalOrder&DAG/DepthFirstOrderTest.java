/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstOrderTest {
    private boolean[] marked;
    private int[] pre;
    private int[] post;
    Queue<Integer> preOrder;
    Queue<Integer> postOrder;
    private int preCount;
    private int postCount;

    public DepthFirstOrderTest(DigraphTest G) {
        marked = new boolean[G.V()];
        pre = new int[G.V()];
        post = new int[G.V()];
        preOrder = new Queue<>();
        postOrder = new Queue<>();

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v])
                dfs(G, v);
        }
    }

    private void dfs(DigraphTest G, int v) {
        marked[v] = true;
        pre[v] = preCount++;
        preOrder.enqueue(v);

        for (int w : G.adj(v)) {
            if (!marked[w])
                dfs(G, w);
        }

        post[v] = postCount++;
        postOrder.enqueue(v);
    }


    public DepthFirstOrderTest(EdgeWeightedDigraphTest G) {
        marked = new boolean[G.V()];
        pre = new int[G.V()];
        post = new int[G.V()];
        preOrder = new Queue<>();
        postOrder = new Queue<>();

        for (int v = 0; v < G.V(); v++)
            if (!marked[v])
                dfs(G, v);
    }

    private void dfs(EdgeWeightedDigraphTest G, int v) {
        marked[v] = true;
        pre[v] = preCount++;
        preOrder.enqueue(v);

        for (DirectedEdgeTest e : G.adj(v)) {
            int w = e.to();
            if (!marked[w])
                dfs(G, w);
        }

        post[v] = postCount++;
        postOrder.enqueue(v);
    }

    public int pre(int v) {
        validateVertex(v);
        return pre[v];
    }

    public int post(int v) {
        validateVertex(v);
        return post[v];
    }

    public Iterable<Integer> pre() {
        return preOrder;
    }

    public Iterable<Integer> post() {
        return postOrder;
    }

    public Iterable<Integer> reversePost() {
        Stack<Integer> reversePost = new Stack<>();
        for (int v : post()) {
            reversePost.push(v);
        }
        return reversePost;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= marked.length)
            throw new IllegalArgumentException("vertex is bot valid");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        DigraphTest G = new DigraphTest(in);

        DepthFirstOrderTest dfs = new DepthFirstOrderTest(G);
        StdOut.println("   v  pre post");
        StdOut.println("--------------");
        for (int v = 0; v < G.V(); v++) {
            StdOut.printf("%4d %4d %4d\n", v, dfs.pre(v), dfs.post(v));
        }

        StdOut.print("Preorder:  ");
        for (int v : dfs.pre()) {
            StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.print("Postorder: ");
        for (int v : dfs.post()) {
            StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.print("Reverse postorder: ");
        for (int v : dfs.reversePost()) {
            StdOut.print(v + " ");
        }
        StdOut.println();


    }
}
