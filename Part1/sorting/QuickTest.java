/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class QuickTest {

    private QuickTest() {

    }

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
        assert isSorted(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int low, int high) {
        if (low >= high) return;
        int j = partition(a, low, high);
        sort(a, low, j - 1);
        sort(a, j + 1, high);
        assert isSorted(a, low, high);
    }

    private static int partition(Comparable[] a, int low, int high) {
        Comparable pivot = a[low];
        int i = low;
        int j = high + 1;

        while (true) {

            // i要從low開始, 且要使用++i, 不能用i++
            while (less(a[++i], pivot)) {
                if (i == high) break;
            }

            // j要從high +1開始, 且使用--j, 不能用j--
            while (less(pivot, a[--j])) {
                if (j == low) break;
            }
            if (i >= j) break;
            exch(a, i, j);
        }
        exch(a, low, j);
        return j;
    }

    private static boolean isSorted(Comparable[] a, int low, int high) {
        for (int i = low; i < high - 1; i++) {
            if (less(a[i + 1], a[i])) return false;
        }
        return true;
    }

    private static boolean less(Comparable u, Comparable v) {
        return u.compareTo(v) < 0;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args) {
        String[] a = { "a", "a", "a", "z", "g", "y", "w", "x" };
        QuickTest.sort(a);
        System.out.println(Arrays.toString(a));
        // for (int i = 0; i < 50; i++) {
        //     Integer[] arr = new Integer[i];
        //     QuickTest.sort(arr);
        //     assert isSorted(arr, 0, arr.length);
        //     System.out.println(arr);
        // }
    }
}
