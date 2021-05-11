/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Quick3WayTest {

    private Quick3WayTest() {

    }

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
        assert isSorted(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int low, int high) {
        if (high <= low)
            return;
        int lt = low, gt = high;
        int i = low + 1;
        while (i < gt) {
            int comp = a[i].compareTo(a[lt]);
            if (comp < 0) {
                exch(a, lt++, i++);
            }
            else if (comp > 0) {
                exch(a, i, gt--);
            }
            else {
                i++;
            }
        }
        sort(a, low, lt - 1);
        sort(a, gt + 1, high);
        assert isSorted(a, low, high);
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean isSorted(Comparable[] a, int low, int high) {
        for (int i = low; i < high; i++) {
            if (less(a[i + 1], a[i])) return false;
        }
        return true;
    }

    private static boolean less(Comparable u, Comparable v) {
        return u.compareTo(v) < 0;
    }

    public static void main(String[] args) {
        Integer[] arr = new Integer[250000000];
        for (int i = 0; i < 250000000; i++) {
            arr[i] = StdRandom.uniform(250000000);
        }
        // Integer[] arr2 = arr.clone();
        // QuickTest.sort(arr);
        // assert isSorted(arr, 0, arr.length - 1);
        // System.out.println(Arrays.toString(arr));
        Arrays.sort(arr);
        // System.out.println(Arrays.toString(arr));
    }
}
