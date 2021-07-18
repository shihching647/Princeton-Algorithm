/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Quick3stringTest {

    private static final int CUT_OFF = 15;

    private Quick3stringTest() {

    }

    public static void sort(String[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1, 0);
        assert isSorted(a);
    }

    private static void sort(String[] a, int lo, int hi, int d) {

        // if (lo >= hi)
        //     return;
        if (hi <= lo + CUT_OFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int v = charAt(a[lo], d);
        int lt = lo, gt = hi;
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a[i], d);
            if (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else i++;
        }

        sort(a, lo, lt - 1, d); // less than要再針對d最一次partition
        if (v >= 0) sort(a, lt, gt, d + 1); // 相同的部分針對下一個character做sort
        sort(a, gt + 1, hi, d); // greater than要再針對d最一次partition
    }

    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo + 1; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--)
                exch(a, j, j - 1);
        }
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

    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    // 如果超過string的長度, 回傳-1
    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    // is the array sorted
    private static boolean isSorted(String[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i].compareTo(a[i - 1]) < 0) return false;
        return true;
    }


    public static void main(String[] args) {

        // read in the strings from standard input
        String[] a = StdIn.readAllStrings();
        int n = a.length;

        // sort the strings
        sort(a);

        // print the results
        for (int i = 0; i < n; i++)
            StdOut.println(a[i]);
    }
}
