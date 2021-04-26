/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Arrays;

public class ShellTest {

    private ShellTest() {

    }

    public static void sort(Comparable[] a) {
        int n = a.length;

        // compute h
        int h = 1;
        while (h < n / 3) h = 3 * h + 1;

        while (h >= 1) {
            for (int i = h; i < n; i += h) {
                for (int j = i; j > 0 && less(a[j], a[j - h]); j -= h)
                    exchange(a, j, j - h);
            }
            assert isHSort(a, h);
            h /= 3;
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

    private static boolean isHSort(Comparable[] a, int h) {
        for (int i = h; i < a.length; i++) {
            if (less(a[i], a[i - h])) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String[] a = { "b", "a", "c", "e", "f", "g", "h", "j", "k" };
        ShellTest.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
