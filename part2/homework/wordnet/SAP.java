/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SAP {
    private boolean[] markedV, markedW;
    private int[] distToV, distToW;
    private Digraph G;
    private boolean hasCommon;
    private int common;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("argument for SAP() is null");

        markedV = new boolean[G.V()];
        markedW = new boolean[G.V()];
        distToV = new int[G.V()];
        distToW = new int[G.V()];
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        reset();
        bfs(v, w);
        if (hasCommon) return distToW[common] + distToV[common];
        else return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        reset();
        bfs(v, w);
        if (hasCommon) return common;
        else return -1;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException("vertex is not valid");
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        reset();
        bfs(v, w);
        if (hasCommon) return distToW[common] + distToV[common];
        else return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        reset();
        bfs(v, w);
        if (hasCommon) return common;
        else return -1;
    }

    private void validate(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException("argument cant not be null");
        for (Integer x : v) {
            if (x == null)
                throw new IllegalArgumentException("iterable argument contains a null item");
            validateVertex(x);
        }
    }


    private void reset() {
        Arrays.fill(markedV, false);
        Arrays.fill(markedW, false);
        Arrays.fill(distToV, 0);
        Arrays.fill(distToW, 0);
    }

    private void bfs(int v, int w) {
        hasCommon = false;
        Queue<Integer> queueV = new Queue<>();
        queueV.enqueue(v);
        markedV[v] = true;
        distToV[v] = 0;

        Queue<Integer> queueW = new Queue<>();
        queueW.enqueue(w);
        markedW[w] = true;
        distToW[w] = 0;

        if (v == w) {
            hasCommon = true;
            common = v;
            return;
        }

        bfs(queueV, queueW);
    }

    private void bfs(Iterable<Integer> v, Iterable<Integer> w) {
        hasCommon = false;
        Queue<Integer> queueV = new Queue<>();
        for (int x : v) {
            queueV.enqueue(x);
            markedV[x] = true;
            distToV[x] = 0;
        }
        Queue<Integer> queueW = new Queue<>();
        for (int x : w) {
            if (markedV[x]) {
                hasCommon = true;
                common = x;
            }
            queueW.enqueue(x);
            markedW[x] = true;
            distToW[x] = 0;
        }

        bfs(queueV, queueW);
    }

    private void bfs(Queue<Integer> queueV, Queue<Integer> queueW) {
        while (!queueV.isEmpty() || !queueW.isEmpty()) {
            if (!queueV.isEmpty()) {
                int x = queueV.dequeue();
                for (int i : G.adj(x)) {
                    if (!markedV[i]) {
                        markedV[i] = true;
                        distToV[i] = distToV[x] + 1;
                        queueV.enqueue(i);
                    }
                    if (markedW[i]) {
                        if (!hasCommon) {
                            hasCommon = true;
                            common = i;
                        }
                        else if (distToV[common] + distToW[common] > distToV[i] + distToW[i]) {
                            common = i;
                        }
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
                        if (!hasCommon) {
                            hasCommon = true;
                            common = i;
                        }
                        else if (distToV[common] + distToW[common] > distToV[i] + distToW[i]) {
                            common = i;
                        }
                    }
                }
                if (hasCommon && distToV[y] + 1 > distToV[common] + distToW[common])
                    break;
            }
        }
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
