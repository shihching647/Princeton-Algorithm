/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class MSDTest {

    private static final int R = 256;   // extended ASCII alphabet size
    private static final int CUTOFF = 15;   // cutoff to insertion sort

    private static final int BITS_PER_INT = 32;
    private static final int BITS_PER_BYTE = 8;


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

        int[] count = new int[R + 2]; // charAt()多一個-1當作String的終止點,
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

    // 如果超過string的長度, 回傳-1
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

    // DEPRECATED BECAUSE OF SLOW SUBSTRING EXTRACTION IN JAVA 7
    // private static boolean less(String v, String w, int d) {
    //    return v.substring(d).compareTo(w.substring(d)) < 0;
    // }

    private static boolean less(String v, String w, int d) {
        int len = Math.min(v.length(), w.length());
        for (int i = d; i < len; i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return v.length() < w.length();
    }

    public static void sort(int[] a) {
        int n = a.length;
        int[] aux = new int[n];
        sort(a, 0, n - 1, 0, aux);
    }

    private static void sort(int[] a, int lo, int hi, int d, int[] aux) {

        // if (lo >= hi)
        //     return;

        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi);
            return;
        }

        int[] count = new int[R + 1];
        int shift = BITS_PER_INT - BITS_PER_BYTE * d - BITS_PER_BYTE; // d == 0時, 移動3個BYTE
        int mask = R - 1;
        for (int i = lo; i <= hi; i++) {
            int c = (a[i] >> shift) & mask;
            count[c + 1]++;
        }

        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

        // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
        if (d == 0) {
            int shift1 = count[R] - count[R / 2]; // shift1 = 負數的個數 (0x80-0xFF)
            int shift2 = count[R / 2];            // shift2 = 正數的個數 (0x00-0x7F)
            count[R] = shift1 + count[1];         // ?????to simplify recursive calls later
            for (int i = 0; i < R / 2; i++)       // 正數的index(0~R/2-1)全部往右移負數的個數(shift1)
                count[i] += shift1;
            for (int i = R / 2; i < R; i++)       // 負數的index(R/2~R-1)全部往左移正數的個數(shift2)
                count[i] -= shift2;
        }

        for (int i = lo; i <= hi; i++) {
            int c = (a[i] >> shift) & mask;
            aux[count[c]++] = a[i];
        }

        System.arraycopy(aux, 0, a, lo, hi - lo + 1);

        // no more bits
        if (d == 3) return;

        // special case for most significant byte????
        if (d == 0 && count[R / 2] > 0)
            sort(a, lo, lo + count[R / 2] - 1, d + 1, aux);

        // special case for other bytes????
        if (d != 0 && count[0] > 0)
            sort(a, lo, lo + count[0] - 1, d + 1, aux);

        // ?????
        // recursively sort for each character
        // (could skip r = R/2 for d = 0 and skip r = R for d > 0)
        for (int r = 0; r < R; r++)
            if (count[r + 1] > count[r])
                sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
    }

    private static void insertion(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            for (int j = i; j > lo && a[j] < a[j - 1]; j--)
                exch(a, j - 1, j);
        }
    }

    private static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args) {
        // String[] a = StdIn.readAllStrings();
        // int n = a.length;
        // sort(a);
        // for (int i = 0; i < n; i++)
        //     StdOut.print(a[i] + ", ");
        // System.out.println();

        int[] nums = new int[1000];
        for (int i = 0; i < nums.length; i++)
            nums[i] = StdRandom.uniform(-100, 100);
        sort(nums);
        System.out.println(Arrays.toString(nums));
    }
}
