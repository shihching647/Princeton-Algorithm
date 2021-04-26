/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Arrays;
import java.util.Comparator;

public class InsertionTest {

    private InsertionTest() {

    }

    // Using Comparable
    public static void sort(Comparable[] a) {
        int n = a.length;
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
                exchange(a, j, j - 1);
            }
            assert isSorted(a, 0, i);
        }
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

    private static boolean isSorted(Comparable[] a, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            if (less(a[i], a[i - 1])) return false;
        }
        return true;
    }


    // Using Comparator
    public static void sort(Object[] a, Comparator comparator) {
        int n = a.length;
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0 && less(comparator, a[j], a[j - 1]); j--) {
                exchange(a, j, j - 1);
            }
        }
    }

    private static boolean less(Comparator comparator, Object u, Object v) {
        return comparator.compare(u, v) < 0;
    }

    private static void exchange(Object[] a, int i, int j) {
        Object temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean isSorted(Object[] a, Comparator comparator) {
        return isSorted(a, 0, a.length - 1, comparator);
    }

    private static boolean isSorted(Object[] a, int low, int high, Comparator comparator) {
        for (int i = low + 1; i <= high; i++) {
            if (less(comparator, a[i], a[i - 1])) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String[] a = { "a", "z", "g", "y", "w", "x" };
        InsertionTest.sort(a);
        System.out.println(Arrays.toString(a));
        System.out.println(isSorted(a));

        Comparator<String> reverseComparator = (o1, o2) -> o2.compareTo(o1);
        InsertionTest.sort(a, reverseComparator);
        System.out.println(Arrays.toString(a));
        System.out.println(isSorted(a, reverseComparator));
    }
}
