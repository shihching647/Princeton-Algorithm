/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MSDTest {

    private static final int R = 256;   // extended ASCII alphabet size
    private static final int CUTOFF = 15;   // cutoff to insertion sort

    private MSDTest() {

    }

    public static void sort(String[] a) {
        int n = a.length;
        String[] aux = new String[n];
        sort(a, 0, n - 1, 0, aux);
    }

    private static void sort(String[] a, int lo, int hi, int d, String[] aux) {

        // if (lo >= hi)
        //     return;

        // cut-off
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            count[c + 2]++;
        }

        for (int i = 0; i < R + 1; i++)
            count[i + 1] += count[i];

        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            aux[count[c + 1]++] = a[i];
        }

        // aux[]存的是以lo為起點的index, 所以在回填的時後aux要從0開始, a要從lo開始
        System.arraycopy(aux, 0, a, lo, hi - lo + 1);

        for (int r = 0; r < R; r++)
            sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
    }

    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    private static void insertion(String[] a, int lo, int hi, int d) {
        // 直接從lo + 1開始就可以了, 第一個lo不需要檢查(但其實這邊從i = lo開始也可以)
        for (int i = lo + 1; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--)
                exch(a, j, j - 1);
        }
    }

    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(String s1, String s2, int d) {
        return s1.substring(d).compareTo(s2.substring(d)) < 0;
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int n = a.length;
        sort(a);
        for (int i = 0; i < n; i++)
            StdOut.print(a[i] + ", ");
        System.out.println();
    }
}
