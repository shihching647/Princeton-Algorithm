/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WeightedQuickUnionTest {
    private int count; // number of components
    private int[] parent;
    private int[] size;

    public WeightedQuickUnionTest(int n) {
        this.count = n;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
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
        int temp = p;
        while (p != parent[p]) {
            p = parent[p];
        }
        // Path compression
        while (temp != p) {
            temp = parent[temp];
            parent[temp] = p;
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
            if (size[pRoot] > size[qRoot]) { // change q root to p root.
                parent[qRoot] = pRoot;
                size[pRoot] += size[qRoot];
            }
            else {                          // change p root to q root.
                parent[pRoot] = qRoot;
                size[qRoot] += size[pRoot];
            }
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
        WeightedQuickUnionTest uf = new WeightedQuickUnionTest(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            // StdOut.println(p + " " + q);
        }
        StdOut.println(uf.count() + " components");
    }
}
