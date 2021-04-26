/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class BoyerMooreTest {
    private final String pattern;
    private final int charSize; // 字串包含不同字元的總數，default = 2^8 = 256
    private final int[] rightmost; // the rightmost index of a char

    public BoyerMooreTest(String pattern) {
        this.charSize = 256;
        this.pattern = pattern;

        // init rightmost
        rightmost = new int[charSize];
        // all characters are initialed to -1
        for (int i = 0; i < rightmost.length; i++) {
            rightmost[i] = -1;
        }
        // go through whole pattern to compute the rightmost index of each character
        for (int i = 0; i < pattern.length(); i++) {
            rightmost[pattern.charAt(i)] = i;
        }
    }

    public int search(String text) {
        int n = text.length();
        int m = pattern.length();

        int skip;
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m - 1; j >= 0; j--) {
                // find a mismatch, compute the skip value
                // (current index - rightmost index of that character)
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    skip = Math.max(1, j - rightmost[text.charAt(i + j)]);
                    break;
                }
            }
            if (skip == 0) return i; // match case
        }
        return n;
    }

    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];

        BoyerMooreTest boyermoore1 = new BoyerMooreTest(pat);
        int offset1 = boyermoore1.search(txt);

        // print results
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}
