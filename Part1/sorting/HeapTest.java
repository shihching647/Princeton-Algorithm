/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Arrays;

public class HeapTest {

    private HeapTest() {

    }

    public static void sort(Comparable[] a) {
        int n = a.length;

        // heapify
        for (int k = n / 2; k >= 1; k--) {
            sink(a, k, n);
        }

        // sortdown phase
        for (int k = n; k >= 1; k--) {
            exch(a, 1, k);
            sink(a, 1, k - 1); // the heap structure now is a[1] ~ a[k - 1]
        }
    }

    private static void sink(Comparable[] a, int k, int n) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && less(a, j, j + 1)) j = j + 1;
            if (!less(a, k, j)) break;
            exch(a, j, k);
            k = j;
        }
    }

    /***************************************************************************
     * Helper functions for comparisons and swaps.
     * Indices are "off-by-one" to support 1-based indexing.
     ***************************************************************************/

    private static boolean less(Comparable[] a, int i, int j) {
        return a[i - 1].compareTo(a[j - 1]) < 0;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i - 1];
        a[i - 1] = a[j - 1];
        a[j - 1] = temp;
    }

    public static void main(String[] args) {
        String[] a = { "a", "a", "a", "z", "g", "y", "a", "w", "x", "a" };
        HeapTest.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
