/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description: 寫得很爛, 有機會要重寫
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

// TODO 寫得很爛, 有機會要重寫

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
        Arrays.fill(distToV, -1);
        Arrays.fill(distToW, -1);
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        bfs(v, w);
        if (hasCommon) return distToW[common] + distToV[common];
        else return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
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
        bfs(v, w);
        if (hasCommon) return distToW[common] + distToV[common];
        else return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
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
        Arrays.fill(distToV, -1);
        Arrays.fill(distToW, -1);
        hasCommon = false;
    }

    private void bfs(int v, int w) {
        // 如果query與上次相同(或v,w交換), 不重複做
        // TODO 加這行 Test 20: random calls to both version of length() and ancestor(), with probabilities p1 and p2, respectively 會fail, 不知道為何
        // if (distToV[v] == 0 && distToW[w] == 0 || distToW[v] == 0 && distToV[w] == 0) {
        //     return;
        // }

        reset();
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
        reset();
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
            int x = -1;
            if (!queueV.isEmpty()) {
                x = queueV.dequeue();
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

            int y = -1;
            if (!queueW.isEmpty()) {
                y = queueW.dequeue();
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
            }

            // v, w個別都同時已經超過最佳解 distToV[common] + distToW[common] -> break
            if (hasCommon && x != -1 && distToV[x] > distToV[common] + distToW[common]
                    && y != -1 && distToW[y] > distToV[common] + distToW[common]) {
                break;
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        Iterable<Integer> v = Arrays.asList(23814, 26923, 75631);
        Iterable<Integer> w = Arrays
                .asList(12986, 13382, 15434, 17440, 35765, 49555, 53352, 60511, 63814, 65338,
                        81335);
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
