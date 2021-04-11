/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class QuickUnionTest {
    private int count; // number of components
    private int[] parent;

    public QuickUnionTest(int n) {
        this.count = n;
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int count() {
        return count;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public int find(int p) {
        validate(p);
        while (p != parent[p]) {
            p = parent[p];
        }
        return p;
    }

    // To merge components containing p and q, change p root to q root.
    public void union(int p, int q) {
        validate(p);
        validate(q);
        int pRoot = find(p);
        int qRoot = find(q);
        if (pRoot != qRoot) {
            parent[pRoot] = qRoot;
            count--;
        }
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException(
                    "index " + p + " is not between 0 and " + (n - 1));
        }
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        QuickUnionTest uf = new QuickUnionTest(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }
        StdOut.println(uf.count() + " components");
    }
}
