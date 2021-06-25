/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private boolean[] markedV, markedW;
    private int[] distToV, distToW;
    private Digraph G;
    private boolean isEnd;
    private int common;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {

        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        markedV = new boolean[G.V()];
        markedW = new boolean[G.V()];
        distToV = new int[G.V()];
        distToW = new int[G.V()];
        bfs(v, w);
        if (isEnd) return distToW[common] + distToV[common];
        else return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        markedV = new boolean[G.V()];
        markedW = new boolean[G.V()];
        distToV = new int[G.V()];
        distToW = new int[G.V()];
        bfs(v, w);
        if (isEnd) return common;
        else return -1;
    }

    private void bfs(int v, int w) {
        isEnd = false;
        Queue<Integer> queueV = new Queue<>();
        queueV.enqueue(v);
        markedV[v] = true;
        distToV[v] = 0;

        Queue<Integer> queueW = new Queue<>();
        queueW.enqueue(w);
        markedW[w] = true;
        distToW[w] = 0;

        while (!isEnd && (!queueV.isEmpty() || !queueW.isEmpty())) {
            if (!queueV.isEmpty()) {
                int x = queueV.dequeue();
                for (int i : G.adj(x)) {
                    if (!markedV[i]) {
                        markedV[i] = true;
                        distToV[i] = distToV[x] + 1;
                        queueV.enqueue(i);
                    }
                    if (markedW[i]) {
                        isEnd = true;
                        common = i;
                        break;
                    }
                }
            }

            if (!queueW.isEmpty()) {
                int y = queueW.dequeue();
                for (int i : G.adj(y)) {
                    if (!markedW[i]) {
                        markedW[i] = true;
                        distToW[i] = distToW[y] + 1;
                        queueW.enqueue(i);
                    }
                    if (markedV[i]) {
                        isEnd = true;
                        common = i;
                        break;
                    }
                }
            }
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
