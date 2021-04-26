/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Arrays;

public class MergeTest {

    private MergeTest() {
    }

    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, Comparable[] aux, int low, int high) {
        if (high <= low) return;
        int mid = low + (high - low) / 2;
        sort(a, aux, low, mid);
        sort(a, aux, mid + 1, high);
        merge(a, aux, low, mid, high);
    }

    private static void merge(Comparable[] a, Comparable[] aux, int low, int mid, int high) {

        assert isSorted(a, low, mid);
        assert isSorted(a, mid + 1, high);

        for (int k = low; k <= high; k++) {
            aux[k] = a[k];
        }
        int i = low;
        int j = mid + 1;
        for (int k = low; k <= high; k++) {
            // 自己寫的
            // j <= high要再前面, 否則a[j]會超過index
            if (i > mid || (j <= high && less(aux[j], aux[i]))) a[k] = aux[j++];
            else a[k] = aux[i++];

            // textbook版
            // if (i > mid) a[k] = aux[j++];
            // else if (j > high) a[k] = aux[i++];
            // else if (less(aux[j], aux[i])) a[k] = aux[j++];
            // else a[k] = aux[i++];
        }
        assert isSorted(a, low, high);
    }

    private static boolean less(Comparable u, Comparable v) {
        return u.compareTo(v) < 0;
    }

    private static void exchange(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    // is the array a[lo..hi) sorted
    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i - 1])) return false;
        return true;
    }

    public static void main(String[] args) {
        String[] a = { "a", "a", "a", "z", "g", "y", "w", "x" };
        MergeTest.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
