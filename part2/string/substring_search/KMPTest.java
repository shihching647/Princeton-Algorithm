/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class KMPTest {
    private final int m;
    private final int charSize; // 字串包含不同字元的總數，default = 2^8 = 256
    private int[][] dfa; // deterministic finite-state automation

    public KMPTest(String pattern) {
        this.m = pattern.length();
        this.charSize = 256;

        // init dfa
        dfa = new int[charSize][m];
        dfa[pattern.charAt(0)][0] = 1;
        for (int x = 0, j = 1; j < m; j++) {
            // mismatch case
            for (int c = 0; c < charSize; c++)
                dfa[c][j] = dfa[c][x];
            // match case
            dfa[pattern.charAt(j)][j] = j + 1;
            // update x state
            x = dfa[pattern.charAt(j)][x];
        }
    }

    public int search(String text) {
        int n = text.length();
        int i, j;
        for (i = 0, j = 0; i < n && j < m; i++) {
            j = dfa[text.charAt(i)][j]; // update j via DFA
        }
        if (j == m) return i - m;
        else return n;
    }

    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text = txt.toCharArray();

        KMPTest kmp1 = new KMPTest(pat);
        int offset1 = kmp1.search(txt);

        // print results
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat);

    }
}
