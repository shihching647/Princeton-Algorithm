/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class BruteTest {

    public static int search1(String pattern, String text) {
        int m = pattern.length();
        int n = text.length();

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) break;
            }
            if (j == m) return i;
        }
        return n;
    }

    public static int search2(String pattern, String text) {
        int m = pattern.length();
        int n = text.length();

        int i, j;
        for (i = 0, j = 0; i < n && j < m; i++) { // 這邊的i等於search1裡的i + j
            if (text.charAt(i) == pattern.charAt(j)) j++;
            else {
                i -= j; // backing up
                j = 0;
            }
        }
        if (j == m) return i - m;
        else return n;
    }

    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text = txt.toCharArray();

        int offset1a = search1(pat, txt);
        int offset2a = search2(pat, txt);

        // print results
        StdOut.println("text:    " + txt);

        // from brute force search method 1a
        StdOut.print("pattern: ");
        for (int i = 0; i < offset1a; i++)
            StdOut.print(" ");
        StdOut.println(pat);

        // from brute force search method 1a
        StdOut.print("pattern: ");
        for (int i = 0; i < offset2a; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}
