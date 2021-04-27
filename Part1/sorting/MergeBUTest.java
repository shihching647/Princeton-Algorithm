/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Arrays;

public class MergeBUTest {
    private MergeBUTest() {
    }

    public static void sort(Comparable[] a) {
        int n = a.length;

        Comparable[] aux = new Comparable[n];
        for (int len = 1; len < n; len *= 2) { // len從1開始才可以 len *= 2
            for (int low = 0; low < n - len; low += (2 * len)) { // low從0開始只能 low += (2 * len)
                int mid = low + len - 1;
                int high = Math.min(mid + len, n - 1); // mid + len可能超過最大index n - 1
                merge(a, aux, low, mid, high);
            }
        }
    }

    private static void merge(Comparable[] a, Comparable[] aux, int low, int mid, int high) {

        // copy elements to aux[]
        for (int i = low; i <= high; i++) {
            aux[i] = a[i];
        }

        int i = low;
        int j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > high) a[k] = aux[i++];
            else if (less(aux[i], aux[j])) a[k] = aux[i++];
            else a[k] = aux[j++];
        }

    }

    private static boolean less(Comparable u, Comparable v) {
        return u.compareTo(v) < 0;
    }

    public static void main(String[] args) {
        String[] a = { "a", "y", "p", "z", "g", "y", "w", "x" };
        MergeBUTest.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
