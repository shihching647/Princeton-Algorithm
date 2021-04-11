/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WeightedQuickUnionVersion2 {
    private int count; // number of components
    private int[] parent; // 正數代表parent的位置, 負數代表為root且數字代表有多少children

    public WeightedQuickUnionVersion2(int n) {
        this.count = n;
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }
    }

    public int count() {
        return count;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    // Return the root index of p
    public int find(int p) {
        validate(p);
        if (parent[p] < 0) {
            return p;
        }
        else {
            parent[p] = find(parent[p]);
            return parent[p];
        }
    }

    public void union(int p, int q) {
        validate(p);
        validate(q);
        int pRoot = find(p);
        int qRoot = find(q);
        if (parent[qRoot] >= 0 || parent[pRoot] >= 0)
            throw new IllegalArgumentException("You should not union two sets with child node.");
        if (pRoot != qRoot) {
            if (parent[qRoot]
                    > parent[pRoot]) { // p have more children than q, change q root to p root.
                parent[pRoot] += parent[qRoot];
                parent[qRoot] = pRoot;
            }
            else {                          // q have more children than p, change p root to q root.
                parent[qRoot] += parent[pRoot];
                parent[pRoot] = qRoot;
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
        WeightedQuickUnionVersion2 uf = new WeightedQuickUnionVersion2(n);
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
