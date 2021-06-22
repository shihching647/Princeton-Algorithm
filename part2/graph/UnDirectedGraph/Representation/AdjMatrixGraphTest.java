/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AdjMatrixGraphTest {
    private int V;
    private int E;
    private boolean[][] adj;

    public AdjMatrixGraphTest(int V) {
        this.V = V;
        this.E = 0;
        adj = new boolean[V][V];
    }

    public AdjMatrixGraphTest(int V, int E) {
        this(V);
        // 每個Vertex可以跟除了自己以外的Vertex有連結, v-w, w-v是一樣的所以要除2, 再加上V(self-edge)
        if (E > (long) V * (V - 1) / 2 + V) throw new IllegalArgumentException("Too many edges");
        if (E < 0) throw new IllegalArgumentException("Too few edges");

        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            addEdge(v, w);
        }
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        if (!adj[v][w]) E++;
        adj[v][w] = true;
        adj[w][v] = true;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public int degree(int v) {
        int degree = 0;
        for (boolean e : adj[v]) {
            if (e) degree++;
        }
        return degree;
    }

    public boolean contains(int v, int w) {
        return adj[v][w];
    }

    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return new AdjIterator(v);
    }

    private class AdjIterator implements Iterator<Integer>, Iterable<Integer> {
        private int v;
        private int w = 0;

        public AdjIterator(int v) {
            this.v = v;
        }

        public Iterator<Integer> iterator() {
            return this;
        }

        public boolean hasNext() {
            while (w < V) {
                if (adj[v][w]) return true;
                w++;
            }
            return false;
        }

        public Integer next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return w++;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("V = " + V + ", E = " + E + "\n");
        for (int i = 0; i < V; i++) {
            sb.append(i + ": ");
            for (int j : adj(i)) {
                sb.append(j + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex is invalid");
    }

    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        AdjMatrixGraphTest G = new AdjMatrixGraphTest(V, E);
        StdOut.println(G);
        StdOut.println(G.contains(0, 4));
        StdOut.println(G.contains(2, 2));
        StdOut.println(G.degree(3));
        StdOut.println(G.degree(1));
    }
}
