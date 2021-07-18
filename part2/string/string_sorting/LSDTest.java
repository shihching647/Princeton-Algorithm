/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class LSDTest {

    private static final int BIT_PER_BYTE = 8;

    private LSDTest() {

    }

    // sort String with equal length w
    public static void sort(String[] a, int w) {
        int R = 256;
        int n = a.length;
        String[] aux = new String[n];

        for (int d = w - 1; d >= 0; d--) {
            int[] count = new int[R + 1];

            for (int i = 0; i < n; i++)
                count[a[i].charAt(d) + 1]++;

            for (int i = 0; i < R; i++)
                count[i + 1] += count[i];

            for (int i = 0; i < n; i++)
                aux[count[a[i].charAt(d)]++] = a[i];

            System.arraycopy(aux, 0, a, 0, n);
        }

    }

    // sort Integer
    public static void sort(int[] a) {
        final int BITS = 32;
        final int R = 1 << BIT_PER_BYTE;
        final int MASK = R - 1;
        final int w = BITS / BIT_PER_BYTE;

        int n = a.length;
        int[] aux = new int[n];

        for (int d = 0; d < w; d++) {
            int[] count = new int[R + 1];

            for (int i = 0; i < n; i++) {
                int c = (a[i] >> BIT_PER_BYTE * d) & MASK;
                count[c + 1]++;
            }

            for (int i = 0; i < R; i++)
                count[i + 1] += count[i];

            // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
            if (d == w - 1) {
                int shift1 = count[R] - count[R / 2]; // shift1 = 負數的個數 (0x80-0xFF)
                int shift2 = count[R / 2];            // shift2 = 正數的個數 (0x00-0x7F)
                for (int i = 0; i < R / 2; i++)       // 正數的index(0~R/2-1)全部往右移負數的個數(shift1)
                    count[i] += shift1;
                for (int i = R / 2; i < R; i++)       // 負數的index(R/2~R-1)全部往左移正數的個數(shift2)
                    count[i] -= shift2;
            }

            for (int i = 0; i < n; i++) {
                int c = (a[i] >> BIT_PER_BYTE * d) & MASK;
                aux[count[c]++] = a[i];
            }

            System.arraycopy(aux, 0, a, 0, n);
        }
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int n = a.length;

        // check that strings have fixed length
        int w = a[0].length();
        for (int i = 0; i < n; i++)
            assert a[i].length() == w : "Strings must have fixed length";

        // sort the strings
        sort(a, w);

        // print results
        for (int i = 0; i < n; i++)
            StdOut.print(a[i] + ", ");

        int[] nums = new int[10];
        for (int i = 0; i < nums.length; i++)
            nums[i] = StdRandom.uniform(-100, 100);
        sort(nums);
        System.out.println(Arrays.toString(nums));
    }
}
