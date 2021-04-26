/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.math.BigInteger;
import java.util.Random;

public class RabinKarpTest {
    private String pattern;   // for Las Vegas version
    private long patternHash; // pattern hash
    private int m;            // pattern length
    private long q;           // modules
    private int r;            // radix
    private long rm;          // = r^(m-1) % q

    public RabinKarpTest(String pattern) {
        this.pattern = pattern;
        m = pattern.length();
        r = 256; // r = 10;
        q = longRandomPrime(); // q = 997;

        // pre-compute for rm = r^(m-1) % q
        rm = 1;
        for (int i = 1; i <= m - 1; i++) {
            rm = (r * rm) % q;
        }
        patternHash = hash(pattern, m);
        // System.out.println("RM = " + rm + ", patHash = " + patternHash);
    }


    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    private long hash(String key, int length) {
        long result = 0;
        for (int i = 0; i < length; i++) {
            result = (result * r + key.charAt(i)) % q;
            // result = (result * r + (key.charAt(i) - '0')) % q;
        }
        return result;
    }

    public int search(String text) {
        int n = text.length();
        long textHash = hash(text, m);
        if (textHash == patternHash) return 0;
        for (int i = 0; i < n - m; i++) {
            // System.out.println("Before = " + textHash);
            // substrate leading digit
            textHash = (textHash + q - rm * text.charAt(i) % q) % q;
            // textHash = (textHash + q - rm * (text.charAt(i) - '0') % q) % q;
            // add tailing digit
            textHash = ((textHash * r) + text.charAt(i + m)) % q;
            // textHash = ((textHash * r) + (text.charAt(i + m) - '0')) % q;
            // System.out.println("After = " + textHash);
            // match
            int offset = i + 1;
            if (textHash == patternHash && check(text, offset))
                return offset;
        }
        return n;
    }

    private boolean check(String text, int offset) {
        for (int i = 0; i < m; i++) {
            if (text.charAt(offset + i) != pattern.charAt(i)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text = txt.toCharArray();

        RabinKarpTest kmp1 = new RabinKarpTest(pat);
        int offset1 = kmp1.search(txt);

        // print results
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}
