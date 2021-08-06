/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;

import java.util.Arrays;

public class CircularSuffixArray {

    private static final int R = 256;
    private static final int CUT_OFF = 0;

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        private final String text;
        private final int start;

        private CircularSuffix(String text, int start) {
            this.text = text;
            this.start = start;
        }

        private int length() {
            return text.length();
        }

        private char charAt(int i) {
            return text.charAt((start + i) % text.length());
        }

        public int compareTo(CircularSuffix that) {
            if (this == that) return 0;  // optimization
            for (int i = 0; i < length(); i++) {
                if (this.charAt(i) < that.charAt(i)) return -1;
                if (this.charAt(i) > that.charAt(i)) return +1;
            }
            return 0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length(); i++)
                sb.append(charAt(i));
            return sb.toString();
        }
    }

    private final String s;
    // private final CircularSuffix[] suffixes;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException(
                    "argument for constructor CircularSuffixArray() is null");
        this.s = s;
        CircularSuffix[] suffixes = new CircularSuffix[length()];
        index = new int[s.length()];
        for (int i = 0; i < length(); i++) {
            suffixes[i] = new CircularSuffix(s, i);
        }

        // 排序
        sort(suffixes, 0, suffixes.length - 1, 0);

        // 回寫index[]
        for (int i = 0; i < length(); i++)
            index[i] = suffixes[i].start;
    }

    // 使用Quick 3-way String sort, 且小於15項時使用insertion sort
    private void sort(CircularSuffix[] suffixes, int lo, int hi, int d) {

        // 需要檢查d > length() - 1, 否則遇到全部都一樣的字母時會進入無限迴圈
        if (d > length() - 1)
            return;

        if (hi - lo <= CUT_OFF) {
            insertion(suffixes, lo, hi, d);
            return;
        }

        char v = suffixes[lo].charAt(d);
        int lt = lo, gt = hi;
        int i = lo + 1;
        while (i <= gt) {
            int t = suffixes[i].charAt(d);
            if (t < v) exch(suffixes, lt++, i++);
            else if (t > v) exch(suffixes, gt--, i);
            else i++;
        }

        sort(suffixes, lo, lt - 1, d);
        sort(suffixes, lt, gt, d + 1);
        sort(suffixes, gt + 1, hi, d);
    }

    private void exch(CircularSuffix[] suffixes, int i, int j) {
        CircularSuffix temp = suffixes[i];
        suffixes[i] = suffixes[j];
        suffixes[j] = temp;
    }

    private void insertion(CircularSuffix[] suffixes, int lo, int hi, int d) {
        for (int i = lo + 1; i <= hi; i++) {
            for (int j = i; j > lo && less(suffixes[j], suffixes[j - 1], d); j--)
                exch(suffixes, j, j - 1);
        }
    }

    private boolean less(CircularSuffix v, CircularSuffix w, int d) {
        for (int i = d; i < length(); i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return false;
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length())
            throw new IllegalArgumentException("argument for index() out of bound");
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = BinaryStdIn.readString();
        CircularSuffixArray test = new CircularSuffixArray(s);
        int[] index = new int[s.length()];
        for (int i = 0; i < index.length; i++)
            index[i] = test.index(i);
        System.out.println(Arrays.toString(index));
    }
}
